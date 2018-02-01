var CACHE_NAME = 'v1';
var urlsToCache = [
'/',
'/css/default_style.css',
'/css/bootstrap.css',
'/css/tracker.js'
];

self.addEventListener('install', function(event) {
  // Perform install steps
  event.waitUntil(
    caches.open(CACHE_NAME)
    .then(function(cache) {
      console.log('Opened cache');
      return cache.addAll(urlsToCache);
    })
    );
});


self.addEventListener('fetch', function(event) {
  event.respondWith(
    caches.match(event.request)
    .then(function(response) {
        // Cache hit - return response
        console.log('request !');
        if (response) {
          return response;
        }
        return fetch(event.request);
      }
      )
    );
});

self.addEventListener('activate', function(event) {

  var cacheWhitelist = ['v1'];

  console.log('activation !');
  event.waitUntil(
    caches.keys().then(function(cacheNames) {
      return Promise.all(
        cacheNames.map(function(cacheName) {
          if (cacheWhitelist.indexOf(cacheName) === -1) {
            return caches.delete(cacheName);
          }
        })
        );
    })
    );
});
