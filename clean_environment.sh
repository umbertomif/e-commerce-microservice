# start clean_up.sh
echo "### Start clean_up.sh"

# stop docker 
echo "### Stop Docker"
docker compose down

sleep 3

# remove docker images
echo "### Remove Docker Images"
docker rmi order
docker rmi orchestration
docker rmi payment
docker rmi inventory
docker rmi shipping
docker rmi notification

sleep 3

# remove target
echo "### Remove Target"
rm -rf order/target
rm -rf orchestration/target
rm -rf payment/target
rm -rf inventory/target
rm -rf shipping/target
rm -rf notification/target

sleep 3

# build target
echo "### Build Target"
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

# finished clean_up.sh
echo "### Finished clean_up.sh"