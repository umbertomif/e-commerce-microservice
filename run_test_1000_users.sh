# Description: This script is used to start the application - 1.000 users

# remove old report
echo "### Remove Old Report"
rm -rf jmeter/report_1000/report
rm -rf jmeter/report_1000/*.csv

sleep 3

echo "### Start Jmeter Test 1.000 Users"
jmeter -n -t ./jmeter/report_1000/microservice_performance_test_1.000.jmx -l ./jmeter/report_1000/summary_report_result_1.000.csv -e -o ./jmeter/report_1000/report

# end script
echo "### End Script"