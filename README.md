### Nodes application

#### Requirements
  * <strong>docker</strong>
  * <strong>docker-compose</strong>

#### How to run the project
  1. `` git clone https://github.com/IvanDim/nodes-application.git ``
  2. `` mvn clean install -DskipTests`` (optional, already completed)
  3. Open terminal in the root directory of the project and run: ``docker build . -t nodes-server``
  4. `` docker-compose up ``  
  5. To run the schema with some example data: `` docker exec -i nodes_docker-mysql_1 sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < src/main/resources/sql/schema.sql `` (your container may have different name than "nodes_docker-mysql_1")