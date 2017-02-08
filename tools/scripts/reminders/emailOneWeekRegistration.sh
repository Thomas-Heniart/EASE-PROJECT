#!/bin/bash

function sendEmail() {
    email="$2"
    name="$1"
    curl --request POST \
	--url https://api.sendgrid.com/v3/mail/send \
	--header 'authorization: Bearer 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' \
	--header 'Content-Type: application/json' \
	--data '{"personalizations": [{"to": [{"email":"'$email'", "name":"'$name'"}], "substitutions": {"#username": "'$name'"}}], "from": {"email":"benjamin@ease.space", "name":"Benjamin"}, "template_id": "435078ae-41d8-4cb1-8dbf-63302f070545"}'
}

#Parenthesis to cast this list of words to array
resultSet=($(mysql -u client -pP6au23q7 ease --batch --skip-column-names -se "SELECT firstName, email FROM (SELECT DISTINCT user_id, firstName, email, COUNT(distinct DATE(date)) AS dateCount FROM logs JOIN users ON (users.id = user_id) WHERE servlet_name LIKE '%AskInfo' AND DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND date BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND NOW() GROUP BY user_id, firstName, email) AS t WHERE t.dateCount >= 2;"))

for i in ${!resultSet[@]};
do
    if [[ $(( $i % 2 )) == 1 ]]; then
	name=${resultSet[$i-1]}
  echo $name
	email=${resultSet[$i]}
	sendEmail $name $email
    fi
done