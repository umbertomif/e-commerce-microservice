# Description: Stop all services

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
