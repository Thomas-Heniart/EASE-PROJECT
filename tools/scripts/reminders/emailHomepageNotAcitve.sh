#!/bin/bash

function sendEmail() {
    email="$2"
    name="$1"
    curl --request POST \
	--url https://api.sendgrid.com/v3/mail/send \
	--header 'authorization: Bearer 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' \
	--header 'Content-Type: application/json' \
	--data '{"personalizations": [{"to": [{"email":"'$email'", "name":"'$name'"}], "substitutions": {"#username": "'$name'", "#linkUrl":"http://localhost:8080/index.jsp?showSettings=true"}}], "from": {"email":"contact@ease.space", "name":"Agathe @Ease"}, "template_id": "961182eb-5b1b-4967-8d91-79950443d8ea"}'
}

#Parenthesis to cast this list of words to array
resultSet=($(mysql -u client -pP6au23q7 ease --batch --skip-column-names -se "SELECT firstName, email FROM (SELECT DISTINCT user_id, firstName, email, count(distinct DATE(date)) AS dateCount FROM logs JOIN users ON (users.id = user_id) JOIN options ON (options.id = users.option_id) JOIN status ON (status.id = users.status_id) WHERE servlet_name LIKE '%AskInfo' AND homepage_state = 0 AND homepage_email_sent = 0 AND date BETWEEN DATE_SUB(CURDATE(), INTERVAL 3 WEEK) AND NOW() GROUP BY user_id, firstName, email) AS t WHERE t.dateCount >= 15;"))

for i in ${!resultSet[@]};
do
    if [[ $(( $i % 2 )) == 1 ]]; then
	name=${resultSet[$i-1]}
	email=${resultSet[$i]}
	sendEmail $name $email
    fi
done;

mysql -u client -pP6au23q7 ease --batch --skip-column-names -se "UPDATE status SET homepage_email_sent = 1 WHERE id IN (SELECT status_id FROM (SELECT DISTINCT user_id, status_id, count(distinct DATE(date)) AS dateCount FROM logs JOIN users ON (users.id = user_id) JOIN options ON (options.id = users.option_id) JOIN status ON (status.id = users.status_id) WHERE servlet_name LIKE '%AskInfo' AND homepage_state = 0 AND homepage_email_sent = 0 AND date BETWEEN DATE_SUB(CURDATE(), INTERVAL 3 WEEK) AND NOW() GROUP BY user_id, firstName, email) AS t WHERE t.dateCount >= 15);"