echo ################################################
echo # Description: Start Jmeter Test - 1.000 users #
echo ################################################

jmeter -n -t ./jmeter/report_1000/microservice_performance_test_1.000.jmx -l ./jmeter/report_1000/summary_report_result_1.000.csv -e -o ./jmeter/report_1000/report

sleep 10

echo #################################################
echo # Description: Start Jmeter Test - 10.000 users #
echo #################################################

jmeter -n -t ./jmeter/report_10000/microservice_performance_test_10.000.jmx -l ./jmeter/report_10000/summary_report_result_10.000.csv -e -o ./jmeter/report_10000/report

sleep 10

echo ##################################################
echo # Description: Start Jmeter Test - 100.000 users #
echo ##################################################

jmeter -n -t ./jmeter/report_100000/microservice_performance_test_100.000.jmx -l ./jmeter/report_100000/summary_report_result_100.000.csv -e -o ./jmeter/report_100000/report

sleep 10

echo ####################################################
echo # Description: Start Jmeter Test - 1.000.000 users #
echo ####################################################

jmeter -n -t ./jmeter/report_1000000/microservice_performance_test_1.000.000.jmx -l ./jmeter/report_1000000/summary_report_result_1.000.000.csv -e -o ./jmeter/report_1000000/report

sleep 10