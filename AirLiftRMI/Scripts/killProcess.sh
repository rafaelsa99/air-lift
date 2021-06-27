 sshpass -f password ssh sd205@l040101-ws$1.ua.pt << EOF
        kill -9 $2
EOF
