# Run Jmeter tester
```
jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
```
## 1.000
```
jmeter -n -t ./report_1000/microservice_performance_test_1.000.jmx -l ./report_1000/summary_report_result_1.000.csv -e -o ./report_1000/report
```
## 10.000
```
jmeter -n -t ./report_10000/microservice_performance_test_10.000.jmx -l ./report_10000/summary_report_result_10.000.csv -e -o ./report_10000/report
```
## 100.000
```
jmeter -n -t ./report_100000/microservice_performance_test_100.000.jmx -l ./report_100000/summary_report_result_100.000.csv -e -o ./report_100000/report
```
## 1.000.000
```
jmeter -n -t ./report_1000000/microservice_performance_test_1.000.000.jmx -l ./report_1000000/summary_report_result_1.000.000.csv -e -o ./report_1000000/report
```