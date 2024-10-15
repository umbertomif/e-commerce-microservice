# Description: This script is used to start the application - 100.000 users

# remove report
echo "### Remove Old Report"
rm -rf jmeter/report_100000/report
rm -rf jmeter/report_100000/*.csv

sleep 3

echo "### Start Jmeter Test 100.000 Users"
jmeter -n -t ./jmeter/report_100000/microservice_performance_test_100.000.jmx -l ./jmeter/report_100000/summary_report_result_100.000.csv -e -o ./jmeter/report_100000/report

# end script
echo "### End Script"