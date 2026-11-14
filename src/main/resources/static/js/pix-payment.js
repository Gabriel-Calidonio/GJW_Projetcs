/**
 * Utilitário front-end para geração de PIX (BRCode + QR Code SVG offline),
 * cópia de código e controle uniforme dos painéis de pagamento no Checkout.
 */
(function (global) {
    'use strict';

    function formatEmvField(id, value) {
        const str = String(value);
        const len = String(str.length).padStart(2, '0');
        return id + len + str;
    }

    function computeCrc16Ccitt(payload) {
        let crc = 0xFFFF;
        for (let i = 0; i < payload.length; i++) {
            crc ^= payload.charCodeAt(i) << 8;
            for (let j = 0; j < 8; j++) {
                if ((crc & 0x8000) !== 0) {
                    crc = ((crc << 1) ^ 0x1021) & 0xFFFF;
                } else {
                    crc = (crc << 1) & 0xFFFF;
                }
            }
        }
        return crc.toString(16).toUpperCase().padStart(4, '0');
    }

    function normalizeValor(valorStr) {
        if (valorStr === null || valorStr === undefined) return '0.00';
        if (typeof valorStr === 'number') {
            return Number.isFinite(valorStr) ? valorStr.toFixed(2) : '0.00';
        }
        let cleaned = String(valorStr).replace(/[^\d.,]/g, '').trim();
        if (!cleaned) return '0.00';
        if (cleaned.includes(',') && cleaned.includes('.')) {
            cleaned = cleaned.replace(/\./g, '').replace(',', '.');
        } else if (cleaned.includes(',')) {
            cleaned = cleaned.replace(',', '.');
        }
        const num = parseFloat(cleaned);
        return Number.isFinite(num) && num >= 0 ? num.toFixed(2) : '0.00';
    }

    /**
     * Gera payload EMVCo PIX ("Pix Copia e Cola") simulado para o valor e identificador informados.
     */
    function generatePixPayload(valorStr, txId) {
        const valorFormatado = normalizeValor(valorStr);
        const safeTxId = String(txId || 'TGOSPIX001')
            .replace(/[^A-Za-z0-9]/g, '')
            .substring(0, 25) || 'TGOSPIX001';

        const gui = formatEmvField('00', 'BR.GOV.BCB.PIX');
        const chave = formatEmvField('01', 'tgos-barbearia-pix@simulacao.com.br');
        const merchantAccount = formatEmvField('26', gui + chave);

        const additionalData = formatEmvField('62', formatEmvField('05', safeTxId));

        let payloadSemCrc = '';
        payloadSemCrc += formatEmvField('00', '01');
        payloadSemCrc += merchantAccount;
        payloadSemCrc += formatEmvField('52', '0000');
        payloadSemCrc += formatEmvField('53', '986');
        if (parseFloat(valorFormatado) > 0) {
            payloadSemCrc += formatEmvField('54', valorFormatado);
        }
        payloadSemCrc += formatEmvField('58', 'BR');
        payloadSemCrc += formatEmvField('59', 'TGOS BARBEARIA');
        payloadSemCrc += formatEmvField('60', 'SAO PAULO');
        payloadSemCrc += additionalData;
        payloadSemCrc += '6304';

        return payloadSemCrc + computeCrc16Ccitt(payloadSemCrc);
    }

    function hashString(str) {
        let h1 = 0x811c9dc5;
        for (let i = 0; i < str.length; i++) {
            h1 ^= str.charCodeAt(i);
            h1 = Math.imul(h1, 0x01000193);
        }
        return h1 >>> 0;
    }

    /**
     * Gera um QR Code SVG 25x25 determinístico a partir do payload (100% offline).
     */
    function generatePixQrSvg(payload, size = 180) {
        const dim = 25;
        const matrix = Array.from({ length: dim }, () => Array(dim).fill(false));
        const reserved = Array.from({ length: dim }, () => Array(dim).fill(false));

        function drawFinder(startRow, startCol) {
            for (let r = -1; r <= 7; r++) {
                for (let c = -1; c <= 7; c++) {
                    const rr = startRow + r;
                    const cc = startCol + c;
                    if (rr < 0 || rr >= dim || cc < 0 || cc >= dim) continue;
                    reserved[rr][cc] = true;
                    if (r === -1 || r === 7 || c === -1 || c === 7) {
                        matrix[rr][cc] = false;
                    } else if (r === 0 || r === 6 || c === 0 || c === 6) {
                        matrix[rr][cc] = true;
                    } else if (r >= 2 && r <= 4 && c >= 2 && c <= 4) {
                        matrix[rr][cc] = true;
                    } else {
                        matrix[rr][cc] = false;
                    }
                }
            }
        }

        drawFinder(0, 0);
        drawFinder(0, dim - 7);
        drawFinder(dim - 7, 0);

        // Timing patterns na linha e coluna 6
        for (let i = 8; i < dim - 8; i++) {
            const dark = i % 2 === 0;
            matrix[6][i] = dark;
            reserved[6][i] = true;
            matrix[i][6] = dark;
            reserved[i][6] = true;
        }

        // Dark module padrão
        matrix[dim - 8][8] = true;
        reserved[dim - 8][8] = true;

        // Preenchimento determinístico dos módulos de dados
        let seed = hashString(payload || 'PIX');
        for (let r = 0; r < dim; r++) {
            for (let c = 0; c < dim; c++) {
                if (reserved[r][c]) continue;
                seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0;
                const charBit = payload ? (payload.charCodeAt((r * dim + c) % payload.length) & 1) : 0;
                matrix[r][c] = ((seed >>> 24) & 1) ^ charBit ? true : false;
            }
        }

        const quietZone = 2;
        const totalView = dim + quietZone * 2;
        let rects = '';
        for (let r = 0; r < dim; r++) {
            for (let c = 0; c < dim; c++) {
                if (matrix[r][c]) {
                    rects += `<rect x="${c + quietZone}" y="${r + quietZone}" width="1" height="1" fill="#111111"/>`;
                }
            }
        }

        return `<svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 ${totalView} ${totalView}" shape-rendering="crispEdges" role="img" aria-label="QR Code PIX Simulado" style="background:#ffffff;border-radius:6px;display:block;">` +
            `<rect width="${totalView}" height="${totalView}" fill="#ffffff"/>` +
            rects +
            `</svg>`;
    }

    function copyToClipboard(text, fallbackInput) {
        if (navigator.clipboard && window.isSecureContext) {
            return navigator.clipboard.writeText(text).catch(() => fallbackCopy(fallbackInput));
        }
        return Promise.resolve(fallbackCopy(fallbackInput));
    }

    function fallbackCopy(inputEl) {
        if (!inputEl) return;
        inputEl.focus();
        inputEl.select();
        try {
            document.execCommand('copy');
        } catch (ignored) {
            // Navegadores sem suporte a execCommand ignoram silenciosamente
        }
    }

    /**
     * Inicializa QR Code PIX, cópia do Copia e Cola e botão opcional de simulação de pagamento.
     */
    function init(options) {
        if (!options) return;
        const qrContainer = document.getElementById(options.qrContainerId);
        const payloadInput = document.getElementById(options.payloadInputId);
        const copyBtn = document.getElementById(options.copyBtnId);
        const copyFeedback = document.getElementById(options.copyFeedbackId);
        const simulateBtn = options.simulateBtnId ? document.getElementById(options.simulateBtnId) : null;
        const simulateBadge = options.simulateBadgeId ? document.getElementById(options.simulateBadgeId) : null;
        const simulatedInput = options.simulatedInputId ? document.getElementById(options.simulatedInputId) : null;

        if (!qrContainer && !payloadInput) return;

        const valor = options.valor !== undefined
            ? options.valor
            : (qrContainer && qrContainer.dataset.pixValor ? qrContainer.dataset.pixValor : '0.00');
        const txId = options.txId !== undefined
            ? options.txId
            : (qrContainer && qrContainer.dataset.pixTxid ? qrContainer.dataset.pixTxid : 'TGOSPIX001');

        const payload = generatePixPayload(valor, txId);

        if (qrContainer) {
            qrContainer.innerHTML = generatePixQrSvg(payload, options.size || 180);
        }
        if (payloadInput) {
            payloadInput.value = payload;
        }

        if (copyBtn) {
            copyBtn.addEventListener('click', () => {
                copyToClipboard(payload, payloadInput).then(() => {
                    if (copyFeedback) {
                        copyFeedback.textContent = '✓ Código PIX copiado!';
                        copyFeedback.style.display = 'inline-block';
                        setTimeout(() => {
                            copyFeedback.style.display = 'none';
                        }, 3000);
                    }
                });
            });
        }

        if (simulateBtn) {
            simulateBtn.addEventListener('click', () => {
                if (simulateBadge) {
                    simulateBadge.style.display = 'block';
                }
                if (simulatedInput) {
                    simulatedInput.value = 'true';
                }
                simulateBtn.disabled = true;
                simulateBtn.textContent = '✓ Pagamento PIX Confirmado (Simulação)';
            });
        }
    }

    /**
     * Aplica máscaras nos campos de cartão de crédito.
     */
    function bindCardMasks(ids) {
        if (!ids) return;
        const cardNumberInput = ids.numberId ? document.getElementById(ids.numberId) : null;
        const cardExpiryInput = ids.expiryId ? document.getElementById(ids.expiryId) : null;
        const cardCvcInput = ids.cvcId ? document.getElementById(ids.cvcId) : null;

        if (cardNumberInput) {
            cardNumberInput.addEventListener('input', (e) => {
                let value = e.target.value.replace(/\D/g, '');
                value = value.replace(/(\d{4})(?=\d)/g, '$1 ');
                e.target.value = value.substring(0, 19);
            });
        }

        if (cardExpiryInput) {
            cardExpiryInput.addEventListener('input', (e) => {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length > 2) {
                    value = value.substring(0, 2) + '/' + value.substring(2, 4);
                }
                e.target.value = value.substring(0, 5);
            });
        }

        if (cardCvcInput) {
            cardCvcInput.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/\D/g, '').substring(0, 4);
            });
        }
    }

    /**
     * Alterna os painéis de detalhes de pagamento e exige campos de cartão apenas quando selecionado.
     */
    function bindPaymentSelector(options) {
        if (!options || !options.radioName || !options.panels) return;
        const radios = document.querySelectorAll(`input[name="${options.radioName}"]`);
        const cardInputs = (options.cardInputIds || []).map((id) => document.getElementById(id));

        const update = () => {
            const checked = document.querySelector(`input[name="${options.radioName}"]:checked`);
            const selected = checked ? checked.value : options.defaultValue;

            Object.entries(options.panels).forEach(([value, panelId]) => {
                const panel = document.getElementById(panelId);
                if (panel) {
                    panel.style.display = selected === value ? 'block' : 'none';
                }
            });

            const isCard = selected === options.cardValue;
            cardInputs.forEach((input) => {
                if (!input) return;
                if (isCard) {
                    input.setAttribute('required', 'required');
                } else {
                    input.removeAttribute('required');
                }
            });
        };

        radios.forEach((radio) => radio.addEventListener('change', update));
        update();
    }

    global.GWJPix = {
        generatePixPayload,
        generatePixQrSvg,
        init,
        bindCardMasks,
        bindPaymentSelector
    };
})(window);

