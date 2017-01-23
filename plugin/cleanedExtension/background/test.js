var json = {
    "highlight": true,
    "hello": "true",
    "detail": [{
        "user": {
            "login": "fel.richart@gmail.com",
            "password": "test"
        },
        "website": {
            "name": "Amazon",
            "lastLogin": "",
            "home": "https://www.amazon.fr/",
            "connect": {
                "todo": [
                    {
                        "action": "aclick",
                        "search": "a[data-nav-ref='nav_ya_signin']"
                },
                    {
                        "action": "fill",
                        "what": "login",
                        "search": "#ap_email"
                },
                    {
                        "action": "fill",
                        "what": "password",
                        "search": "#ap_password"
                },
                    {
                        "action": "click",
                        "search": "#signInSubmit"
                }
			]
            },
            "logout": {
                "todo": [{
                    "action": "goto",
                    "url": "https://www.amazon.fr/gp/flex/sign-out.html/ref=nav__gno_signout?ie=UTF8&action=sign-out&path=%2Fgp%2Fyourstore%2Fhome"
		}]
            },
            "checkAlreadyLogged": [
                {
                    "search": "a[href='/gp/css/homepage.html/ref=nav_youraccount_btn']"
            }
	],
            "checkChangeLogin": [
                {
                    "action": "click",
                    "search": "#signInSubmit"
            }
	]
        }
    }]
}
