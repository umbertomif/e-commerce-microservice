# Executing the stop.sh
echo "### Running stop.sh script"

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

# finished clean_up.sh
echo "### Finished clean_up.sh"