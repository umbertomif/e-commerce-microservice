# Description: This script is used to start the application - 10.000 users

# remove old report
echo "### Remove Old Report"
rm -rf jmeter/report_10000/report
rm -rf jmeter/report_10000/*.csv

sleep 3

echo "### Start Jmeter Test 10.000 Users"
jmeter -n -t ./jmeter/report_10000/microservice_performance_test_10.000.jmx -l ./jmeter/report_10000/summary_report_result_10.000.csv -e -o ./jmeter/report_10000/report

# end script
echo "### End Script"