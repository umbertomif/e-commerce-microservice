#############################################################
# Description: This script is used to start the application #
#############################################################

echo "# running clean_environment.sh script"
sh clean_environment.sh

sleep 3

echo "### Running Docker Compose"
docker compose up -d