import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
    use: {
        /* Define gravação de vídeo */
        video: {
            mode: 'on', // 'on', 'retain-on-failure', ou 'on-first-retry'
            size: { width: 1920, height: 1080 }
        },
        /* Viewport precisa ter o mesmo tamanho para evitar letterboxing */
        viewport: { width: 1920, height: 1080 },
        headless: false,
        launchOptions: {
            slowMo: 400, // Deixa a navegação suave para demonstração
        }
    },
    outputDir: 'test-results/videos',
});