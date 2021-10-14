
ip="18.118.146.52"

echo $ip
scp -i "myKeyForJade.pem" run_experiments.sh ec2-user@$ip:run_experiments.sh