# Description: This script is used to start the application

# stop docker 
docker compose down

# remove docker images
docker rmi order
docker rmi orchestration
docker rmi payment
docker rmi inventory
docker rmi shipping
docker rmi notification

# remove target
rm -rf order/target
rm -rf orchestration/target
rm -rf payment/target
rm -rf inventory/target
rm -rf shipping/target
rm -rf notification/target

# build target
cd order
mvn clean package -DskipTests
cd ..
cd orchestration
mvn clean package -DskipTests
cd ..
cd payment
mvn clean package -DskipTests
cd ..
cd inventory
mvn clean package -DskipTests
cd ..
cd shipping
mvn clean package -DskipTests
cd ..
cd notification
mvn clean package -DskipTests
cd ..

# start docker
docker compose up -d
