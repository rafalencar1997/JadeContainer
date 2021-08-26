
ip="18.119.129.180"

echo $ip
scp -i "myKeyForJade.pem" run_experiments.sh ec2-user@$ip:run_experiments.sh