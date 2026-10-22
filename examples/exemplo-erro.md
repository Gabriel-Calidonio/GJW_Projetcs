wattrelos@Pavilion:/var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico$ ./mvnw spotbugs:check  
[INFO] Scanning for projects...  
[INFO]  
[INFO] --------------------------< com.gwj:projeto >---------------------------  
[INFO] Building projeto 0.0.1-SNAPSHOT  
[INFO]   from pom.xml  
[INFO] --------------------------------[ jar ]---------------------------------  
[INFO]  
[INFO] >>> spotbugs:4.8.4.0:check (default-cli) > :spotbugs @ projeto >>>  
[INFO]  
[INFO] --- spotbugs:4.8.4.0:spotbugs (spotbugs) @ projeto ---  
[INFO] Fork Value is true  
[INFO] Done SpotBugs Analysis....  
[INFO]  
[INFO] <<< spotbugs:4.8.4.0:check (default-cli) < :spotbugs @ projeto <<<  
[INFO]  
[INFO]  
[INFO] --- spotbugs:4.8.4.0:check (default-cli) @ projeto ---  
[INFO] BugInstance size is 56  
[INFO] Error size is 0  
[INFO] Total bugs: 56  
[ERROR] Medium: Primitive field com.gwj.AppConfig.DB_DRIVER is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 16] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Primitive field com.gwj.AppConfig.DB_PASS is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 15] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Primitive field com.gwj.AppConfig.DB_URL is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 13] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Primitive field com.gwj.AppConfig.DB_USER is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 14] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Primitive field com.gwj.AppConfig.ENTITIES_PATH is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 18] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Primitive field com.gwj.AppConfig.TABLE_PREFIX is public and set from inside the class, which makes it too exposed. Consider making it private to limit external accessibility. [com.gwj.AppConfig] At AppConfig.java:[line 17] PA_PUBLIC_PRIMITIVE_ATTRIBUTE  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.AppConfig.reload() [com.gwj.AppConfig] At AppConfig.java:[line 41] REC_CATCH_EXCEPTION  
[ERROR] Medium: Exception thrown in class com.gwj.model.dataAccessObject.ConnectionDB at new com.gwj.model.dataAccessObject.ConnectionDB() will leave the constructor. The object under construction remains partially initialized and may be vulnerable to Finalizer attacks. [com.gwj.model.dataAccessObject.ConnectionDB, com.gwj.model.dataAccessObject.ConnectionDB] At ConnectionDB.java:[line 20]At ConnectionDB.java:[line 20] CT_CONSTRUCTOR_THROW  
[ERROR] Medium: Exception thrown in class com.gwj.model.dataAccessObject.ConnectionPostGreSQL at new com.gwj.model.dataAccessObject.ConnectionPostGreSQL() will leave the constructor. The object under construction remains partially initialized and may be vulnerable to Finalizer attacks. [com.gwj.model.dataAccessObject.ConnectionPostGreSQL, com.gwj.model.dataAccessObject.ConnectionPostGreSQL] At ConnectionPostGreSQL.java:[line 16]At ConnectionPostGreSQL.java:[line 16] CT_CONSTRUCTOR_THROW  
[ERROR] Medium: com.gwj.model.dataAccessObject.QueryBuilder.columns(List) may expose internal representation by storing an externally mutable object into QueryBuilder.columns [com.gwj.model.dataAccessObject.QueryBuilder] At QueryBuilder.java:[line 46] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.dataAccessObject.QueryBuilder.set(List) may expose internal representation by storing an externally mutable object into QueryBuilder.setClauses [com.gwj.model.dataAccessObject.QueryBuilder] At QueryBuilder.java:[line 51] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.Carrinho.getItens() may expose internal representation by returning Carrinho.itens [com.gwj.model.domain.Carrinho] At Carrinho.java:[line 16] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.CarrinhoItem.getProduto() may expose internal representation by returning CarrinhoItem.produto [com.gwj.model.domain.CarrinhoItem] At CarrinhoItem.java:[line 21] EI_EXPOSE_REP  
[ERROR] Medium: new com.gwj.model.domain.CarrinhoItem(Produto, int) may expose internal representation by storing an externally mutable object into CarrinhoItem.produto [com.gwj.model.domain.CarrinhoItem] At CarrinhoItem.java:[line 16] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.CarrinhoItem.setProduto(Produto) may expose internal representation by storing an externally mutable object into CarrinhoItem.produto [com.gwj.model.domain.CarrinhoItem] At CarrinhoItem.java:[line 25] EI_EXPOSE_REP2  
[ERROR] High: Class com.gwj.model.domain.CarrinhoItem defines non-transient non-serializable instance field produto [com.gwj.model.domain.CarrinhoItem] In CarrinhoItem.java SE_BAD_FIELD  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.getGradeHorarios() may expose internal representation by returning Agendamento.gradeHorarios [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 138] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.getProfissional() may expose internal representation by returning Agendamento.profissional [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 122] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.getServico() may expose internal representation by returning Agendamento.servico [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 130] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.setGradeHorarios(GradeHorarios) may expose internal representation by storing an externally mutable object into Agendamento.gradeHorarios [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 142] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.setProfissional(Profissional) may expose internal representation by storing an externally mutable object into Agendamento.profissional [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 126] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Agendamento.setServico(Servico) may expose internal representation by storing an externally mutable object into Agendamento.servico [com.gwj.model.domain.entities.Agendamento] At Agendamento.java:[line 134] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Cliente.getListaEndereco() may expose internal representation by returning Cliente.listaEndereco [com.gwj.model.domain.entities.Cliente] At Cliente.java:[line 84] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Cliente.setListaEndereco(List) may expose internal representation by storing an externally mutable object into Cliente.listaEndereco [com.gwj.model.domain.entities.Cliente] At Cliente.java:[line 88] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.DiasFuncionamento.getGradeHorarios() may expose internal representation by returning DiasFuncionamento.gradeHorarios [com.gwj.model.domain.entities.DiasFuncionamento] At DiasFuncionamento.java:[line 101] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.DiasFuncionamento.setGradeHorarios(List) may expose internal representation by storing an externally mutable object into DiasFuncionamento.gradeHorarios [com.gwj.model.domain.entities.DiasFuncionamento] At DiasFuncionamento.java:[line 105] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.GradeHorarios.getDiaFuncionamento() may expose internal representation by returning GradeHorarios.diaFuncionamento [com.gwj.model.domain.entities.GradeHorarios] At GradeHorarios.java:[line 46] EI_EXPOSE_REP  
[ERROR] Medium: new com.gwj.model.domain.entities.GradeHorarios(DiasFuncionamento, LocalTime, LocalTime) may expose internal representation by storing an externally mutable object into GradeHorarios.diaFuncionamento [com.gwj.model.domain.entities.GradeHorarios] At GradeHorarios.java:[line 31] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.GradeHorarios.setDiaFuncionamento(DiasFuncionamento) may expose internal representation by storing an externally mutable object into GradeHorarios.diaFuncionamento [com.gwj.model.domain.entities.GradeHorarios] At GradeHorarios.java:[line 50] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.ItemPedido.getPedido() may expose internal representation by returning ItemPedido.pedido [com.gwj.model.domain.entities.ItemPedido] At ItemPedido.java:[line 71] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.ItemPedido.getProduto() may expose internal representation by returning ItemPedido.produto [com.gwj.model.domain.entities.ItemPedido] At ItemPedido.java:[line 79] EI_EXPOSE_REP  
[ERROR] Medium: new com.gwj.model.domain.entities.ItemPedido(Produto, Integer) may expose internal representation by storing an externally mutable object into ItemPedido.produto [com.gwj.model.domain.entities.ItemPedido] At ItemPedido.java:[line 37] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.ItemPedido.setPedido(Pedido) may expose internal representation by storing an externally mutable object into ItemPedido.pedido [com.gwj.model.domain.entities.ItemPedido] At ItemPedido.java:[line 75] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.ItemPedido.setProduto(Produto) may expose internal representation by storing an externally mutable object into ItemPedido.produto [com.gwj.model.domain.entities.ItemPedido] At ItemPedido.java:[line 83] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Pedido.getCliente() may expose internal representation by returning Pedido.cliente [com.gwj.model.domain.entities.Pedido] At Pedido.java:[line 64] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Pedido.getItens() may expose internal representation by returning Pedido.itens [com.gwj.model.domain.entities.Pedido] At Pedido.java:[line 120] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Pedido.setCliente(Cliente) may expose internal representation by storing an externally mutable object into Pedido.cliente [com.gwj.model.domain.entities.Pedido] At Pedido.java:[line 68] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Pedido.setItens(List) may expose internal representation by storing an externally mutable object into Pedido.itens [com.gwj.model.domain.entities.Pedido] At Pedido.java:[line 124] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Perfil.getPermissoes() may expose internal representation by returning Perfil.permissoes [com.gwj.model.domain.entities.Perfil] At Perfil.java:[line 60] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Perfil.setPermissoes(List) may expose internal representation by storing an externally mutable object into Perfil.permissoes [com.gwj.model.domain.entities.Perfil] At Perfil.java:[line 64] EI_EXPOSE_REP2  
[ERROR] High: Self assignment of field Produto.imagem in new com.gwj.model.domain.entities.Produto(Long, String, String, BigDecimal, Integer, String, String) [com.gwj.model.domain.entities.Produto] At Produto.java:[line 61] SA_FIELD_SELF_ASSIGNMENT  
[ERROR] Medium: Uninitialized read of imagem in new com.gwj.model.domain.entities.Produto(Long, String, String, BigDecimal, Integer, String, String) [com.gwj.model.domain.entities.Produto] At Produto.java:[line 61] UR_UNINIT_READ  
[ERROR] Medium: com.gwj.model.domain.entities.Profissional.getListaEndereco() may expose internal representation by returning Profissional.listaEndereco [com.gwj.model.domain.entities.Profissional] At Profissional.java:[line 78] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Profissional.setListaEndereco(List) may expose internal representation by storing an externally mutable object into Profissional.listaEndereco [com.gwj.model.domain.entities.Profissional] At Profissional.java:[line 82] EI_EXPOSE_REP2  
[ERROR] Medium: com.gwj.model.domain.entities.Usuario.getPerfil() may expose internal representation by returning Usuario.perfil [com.gwj.model.domain.entities.Usuario] At Usuario.java:[line 46] EI_EXPOSE_REP  
[ERROR] Medium: com.gwj.model.domain.entities.Usuario.setPerfil(Perfil) may expose internal representation by storing an externally mutable object into Usuario.perfil [com.gwj.model.domain.entities.Usuario] At Usuario.java:[line 50] EI_EXPOSE_REP2  
[ERROR] Medium: Possible null pointer dereference in com.gwj.model.domain.factory.SchemaValidator.getClassesFromPackage(String) due to return value of called method [com.gwj.model.domain.factory.SchemaValidator, com.gwj.model.domain.factory.SchemaValidator] Dereferenced at SchemaValidator.java:[line 105]Known null at SchemaValidator.java:[line 105] NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE  
[ERROR] Medium: Return value of String.endsWith(String) ignored in com.gwj.model.domain.factory.SchemaValidator.validateNamingConvention(Class) [com.gwj.model.domain.factory.SchemaValidator] At SchemaValidator.java:[line 86] RV_RETURN_VALUE_IGNORED  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.model.repository.GenericRepository.buildWhereClause(IEntity) [com.gwj.model.repository.GenericRepository] At GenericRepository.java:[line 487] REC_CATCH_EXCEPTION  
[ERROR] Medium: Useless object stored in variable placeholders of method com.gwj.model.repository.GenericRepository.insertForClass(Connection, IEntity, Class, Long) [com.gwj.model.repository.GenericRepository] At GenericRepository.java:[line 48] UC_USELESS_OBJECT  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.service.AgendamentoService.getHorariosDisponiveis(Long, Long, String) [com.gwj.service.AgendamentoService] At AgendamentoService.java:[line 186] REC_CATCH_EXCEPTION  
[ERROR] Medium: new com.gwj.service.GenericService(IRepository) may expose internal representation by storing an externally mutable object into GenericService.repository [com.gwj.service.GenericService] At GenericService.java:[line 18] EI_EXPOSE_REP2  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.service.SetupService.runSchemaScripts(Connection, boolean) [com.gwj.service.SetupService] At SetupService.java:[line 325] REC_CATCH_EXCEPTION  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.tools.CheckEnvironment.checkDatabaseSocket(String, int) [com.gwj.tools.CheckEnvironment] At CheckEnvironment.java:[line 194] REC_CATCH_EXCEPTION  
[ERROR] Medium: Exception is caught when Exception is not thrown in com.gwj.tools.CheckEnvironment.checkWebPort(Properties) [com.gwj.tools.CheckEnvironment] At CheckEnvironment.java:[line 169] REC_CATCH_EXCEPTION  
[ERROR] High: com.gwj.tools.CheckEnvironment.checkDatabaseTablesAndData(Connection, String, String) passes a nonconstant String to an execute or addBatch method on an SQL statement [com.gwj.tools.CheckEnvironment] At CheckEnvironment.java:[line 324] SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE  
[INFO]  
  
  
To see bug detail using the Spotbugs GUI, use the following command "mvn spotbugs:gui"  
  
  
  
[INFO] ------------------------------------------------------------------------  
[INFO] BUILD FAILURE  
[INFO] ------------------------------------------------------------------------  
[INFO] Total time:  17.990 s  
[INFO] Finished at: 2026-10-07T15:55:30-03:00  
[INFO] ------------------------------------------------------------------------  
[ERROR] Failed to execute goal com.github.spotbugs:spotbugs-maven-plugin:4.8.4.0:check (default-cli) on project projeto: failed with 56 bugs and 0 errors  -> [Help 1]  
[ERROR]  
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.  
[ERROR] Re-run Maven using the -X switch to enable full debug logging.  
[ERROR]  
[ERROR] For more information about the errors and possible solutions, please read the following articles:  
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException  