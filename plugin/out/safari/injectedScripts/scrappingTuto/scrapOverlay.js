if (window.location.href.indexOf("https://passwords.google.com/") == 0 || window.location.href.indexOf("https://accounts.google.com/") == 0 || window.location.href.indexOf("https://myaccount.google.com/") == 0 || window.location.href.indexOf("https://www.linkedin.com") == 0 || window.location.href.indexOf("https://www.facebook.com") == 0) {

    var overlayscrap = {
        elements: {},
        sites: {
            "Linkedin": {
                titleWebsite: "Importing accounts",
                titleWebsite2: "you connected with",
                textWebsite: "Sign in with Linkedin",
                logoName: "linkedin.png",
                websiteColor: "3b5998"
            },
            "Facebook": {
                titleWebsite: "Importing accounts",
                titleWebsite2: "you connected with",
                textWebsite: "Sign in with Facebook",
                logoName: "facebook.png",
                websiteColor: "3b5998"
            },
            "Chrome": {
                titleWebsite: "Importing accounts saved in",
                titleWebsite2: false,
                textWebsite: "Google Chrome",
                logoName: "chrome.png",
                websiteColor: false
            }
        },
        create: function (site) {
            this.elements.main = document.createElement('div');
            this.elements.main.id = "ease_overlay_scrap";
            document.body.appendChild(this.elements.main);

            var textWebsite = this.sites[site].textWebsite;
            var logoWebsite = "ressources/" + this.sites[site].logoName;
            var titleWebsite = this.sites[site].titleWebsite;
            var titleWebsite2 = this.sites[site].titleWebsite2;
            var websiteColor = this.sites[site].websiteColor;

            this.elements.main.className = "overlayScrap";
            var container = document.createElement('div');
            container.className = "containerScrap";
            this.elements.main.appendChild(container);
            this.elements.container = container;

            var logoEase = document.createElement('img');
            logoEase.src = extension.getRessource('ressources/logo.png');
            logoEase.className = "logoEase";
            container.appendChild(logoEase);
            this.elements.logoEase = logoEase;

            var titleContainer = document.createElement('div');
            titleContainer.className = "titleContainer";
            container.appendChild(titleContainer);
            this.elements.titleContainer = titleContainer;

            var title = document.createElement('p');
            title.className = "title";
            title.textContent = titleWebsite;
            titleContainer.appendChild(title);
            this.elements.title = title;

            if (titleWebsite2) {
                var title2 = document.createElement('p');
                title2.className = "title";
                title2.textContent = titleWebsite2;
                titleContainer.appendChild(title2);
                this.elements.title2 = title2;
            }

            var websiteContainer = document.createElement('div');
            websiteContainer.className = "websiteContainer";
            if (websiteColor) {
                websiteContainer.style = "background-color:#" + websiteColor + "; border-color:#" + websiteColor;
            }
            container.appendChild(websiteContainer);
            this.elements.websiteContainer = websiteContainer;

            var websiteLogo = document.createElement('img');
            websiteLogo.src = extension.getRessource(logoWebsite);
            websiteContainer.appendChild(websiteLogo);
            this.elements.websiteLogo = websiteLogo;

            var websiteDescription = document.createElement('span');
            websiteDescription.textContent = textWebsite;
            if (websiteColor) {
                websiteDescription.style = "color:white;";
            }
            websiteContainer.appendChild(websiteDescription);
            this.elements.websiteDescription = websiteDescription;

            var loader = document.createElement('div');
            loader.className = "loader";
            container.appendChild(loader);
            this.elements.loader = loader;

            var infoContainer = document.createElement('div');
            infoContainer.className = "infoContainer";
            container.appendChild(infoContainer);
            this.elements.infoContainer = infoContainer;

            var info = document.createElement('p');
            info.textContent = "You’ll select the ones you want to";
            infoContainer.append(info);
            this.elements.info = info;

            var info2 = document.createElement('p');
            info2.textContent = "keep right after this.";
            infoContainer.append(info2);
            this.elements.info2 = info2;
        },
        remove: function (type) {
            this.elements.main.style = "display: none";
        }
    }
}

