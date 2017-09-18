<html xmlns="http://w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
	<title>Welcome on ease !</title>
	<script src='https://cdn.rawgit.com/naptha/tesseract.js/1.0.10/dist/tesseract.js'></script>
</head>
<BODY>
<script>
	!function(e){if("object"==typeof exports&&"undefined"!=typeof module)module.exports=e();else if("function"==typeof define&&define.amd)define([],e);else{var t;t="undefined"!=typeof window?window:"undefined"!=typeof global?global:"undefined"!=typeof self?self:this,t.Tesseract=e()}}(function(){return function e(t,r,n){function o(i,a){if(!r[i]){if(!t[i]){var c="function"==typeof require&&require;if(!a&&c)return c(i,!0);if(s)return s(i,!0);var u=Error("Cannot find module '"+i+"'");throw u.code="MODULE_NOT_FOUND",u}var l=r[i]={exports:{}};t[i][0].call(l.exports,function(e){var r=t[i][1][e];return o(r?r:e)},l,l.exports,e,t,r,n)}return r[i].exports}for(var s="function"==typeof require&&require,i=0;i<n.length;i++)o(n[i]);return o}({1:[function(e,t){"use strict";function r(e){if(null===e||void 0===e)throw new TypeError("Object.assign cannot be called with null or undefined");return Object(e)}function n(){try{if(!Object.assign)return!1;var e=new String("abc");if(e[5]="de","5"===Object.getOwnPropertyNames(e)[0])return!1;for(var t={},r=0;10>r;r++)t["_"+String.fromCharCode(r)]=r;var n=Object.getOwnPropertyNames(t).map(function(e){return t[e]});if("0123456789"!==n.join(""))return!1;var o={};return"abcdefghijklmnopqrst".split("").forEach(function(e){o[e]=e}),"abcdefghijklmnopqrst"!==Object.keys(Object.assign({},o)).join("")?!1:!0}catch(s){return!1}}var o=Object.prototype.hasOwnProperty,s=Object.prototype.propertyIsEnumerable;t.exports=n()?Object.assign:function(e){for(var t,n,i=r(e),a=1;a<arguments.length;a++){t=Object(arguments[a]);for(var c in t)o.call(t,c)&&(i[c]=t[c]);if(Object.getOwnPropertySymbols){n=Object.getOwnPropertySymbols(t);for(var u=0;u<n.length;u++)s.call(t,n[u])&&(i[n[u]]=t[n[u]])}}return i}},{}],2:[function(e,t){function r(){throw Error("setTimeout has not been defined")}function n(){throw Error("clearTimeout has not been defined")}function o(e){if(l===setTimeout)return setTimeout(e,0);if((l===r||!l)&&setTimeout)return l=setTimeout,setTimeout(e,0);try{return l(e,0)}catch(t){try{return l.call(null,e,0)}catch(t){return l.call(this,e,0)}}}function s(e){if(f===clearTimeout)return clearTimeout(e);if((f===n||!f)&&clearTimeout)return f=clearTimeout,clearTimeout(e);try{return f(e)}catch(t){try{return f.call(null,e)}catch(t){return f.call(this,e)}}}function i(){g&&p&&(g=!1,p.length?d=p.concat(d):b=-1,d.length&&a())}function a(){if(!g){var e=o(i);g=!0;for(var t=d.length;t;){for(p=d,d=[];++b<t;)p&&p[b].run();b=-1,t=d.length}p=null,g=!1,s(e)}}function c(e,t){this.fun=e,this.array=t}function u(){}var l,f,h=t.exports={};!function(){try{l="function"==typeof setTimeout?setTimeout:r}catch(e){l=r}try{f="function"==typeof clearTimeout?clearTimeout:n}catch(e){f=n}}();var p,d=[],g=!1,b=-1;h.nextTick=function(e){var t=Array(arguments.length-1);if(arguments.length>1)for(var r=1;r<arguments.length;r++)t[r-1]=arguments[r];d.push(new c(e,t)),1!==d.length||g||o(a)},c.prototype.run=function(){this.fun.apply(null,this.array)},h.title="browser",h.browser=!0,h.env={},h.argv=[],h.version="",h.versions={},h.on=u,h.addListener=u,h.once=u,h.off=u,h.removeListener=u,h.removeAllListeners=u,h.emit=u,h.binding=function(){throw Error("process.binding is not supported")},h.cwd=function(){return"/"},h.chdir=function(){throw Error("process.chdir is not supported")},h.umask=function(){return 0}},{}],3:[function(e,t){t.exports={name:"tesseract.js",version:"1.0.10",description:"Pure Javascript Multilingual OCR",main:"src/index.js",scripts:{test:'echo "Error: no test specified" & exit 1',start:"watchify src/index.js  -t [ envify --NODE_ENV development ] -t [ babelify --presets [ es2015 ] ] -o dist/tesseract.dev.js --standalone Tesseract & watchify src/browser/worker.js  -t [ envify --NODE_ENV development ] -t [ babelify --presets [ es2015 ] ] -o dist/worker.dev.js & http-server -p 7355",build:"browserify src/index.js -t [ babelify --presets [ es2015 ] ] -o dist/tesseract.js --standalone Tesseract && browserify src/browser/worker.js -t [ babelify --presets [ es2015 ] ] -o dist/worker.js",release:"npm run build && git commit -am 'new release' && git push && git tag `jq -r '.version' package.json` && git push origin --tags && npm publish"},browser:{"./src/node/index.js":"./src/browser/index.js"},author:"",license:"Apache-2.0",devDependencies:{"babel-preset-es2015":"^6.16.0",babelify:"^7.3.0",browserify:"^13.1.0",envify:"^3.4.1","http-server":"^0.9.0",pako:"^1.0.3",watchify:"^3.7.0"},dependencies:{"file-type":"^3.8.0","is-url":"^1.2.2","jpeg-js":"^0.2.0","level-js":"^2.2.4","node-fetch":"^1.6.3","object-assign":"^4.1.0","png.js":"^0.2.1","tesseract.js-core":"^1.0.2"},repository:{type:"git",url:"https://github.com/naptha/tesseract.js.git"},bugs:{url:"https://github.com/naptha/tesseract.js/issues"},homepage:"https://github.com/naptha/tesseract.js"}},{}],4:[function(e,t,r){(function(t){"use strict";function n(e,t){if("string"==typeof e){if(/^\#/.test(e))return n(document.querySelector(e),t);if(/(blob|data)\:/.test(e)){var r=new Image;return r.src=e,void(r.onload=function(){return n(r,t)})}var o=new XMLHttpRequest;return o.open("GET",e,!0),o.responseType="blob",o.onload=function(){return n(o.response,t)},o.onerror=function(){/^https?:\/\//.test(e)&&!/^https:\/\/crossorigin.me/.test(e)&&(console.debug("Attempting to load image with CORS proxy"),n("https://crossorigin.me/"+e,t))},void o.send(null)}if(e instanceof File){var s=new FileReader;return s.onload=function(){return n(s.result,t)},void s.readAsDataURL(e)}if(e instanceof Blob)return n(URL.createObjectURL(e),t);if(e.getContext)return n(e.getContext("2d"),t);if("IMG"==e.tagName||"VIDEO"==e.tagName){var i=document.createElement("canvas");i.width=e.naturalWidth||e.videoWidth,i.height=e.naturalHeight||e.videoHeight;var a=i.getContext("2d");return a.drawImage(e,0,0),n(a,t)}if(e.getImageData){var c=e.getImageData(0,0,e.canvas.width,e.canvas.height);return n(c,t)}return t(e)}var o={corePath:"https://cdn.rawgit.com/naptha/tesseract.js-core/0.1.0/index.js",langPath:"https://cdn.rawgit.com/naptha/tessdata/gh-pages/3.02/"};if("development"===t.env.NODE_ENV)console.debug("Using Development Configuration"),o.workerPath=location.protocol+"//"+location.host+"/dist/worker.dev.js?nocache="+Math.random().toString(36).slice(3);else{var s=e("../../package.json").version;o.workerPath="https://cdn.rawgit.com/naptha/tesseract.js/"+s+"/dist/worker.js"}r.defaultOptions=o,r.spawnWorker=function(e,t){if(window.Blob&&window.URL)var r=new Blob(['importScripts("'+t.workerPath+'");']),n=new Worker(window.URL.createObjectURL(r));else var n=new Worker(t.workerPath);return n.onmessage=function(t){var r=t.data;e._recv(r)},n},r.terminateWorker=function(e){e.worker.terminate()},r.sendPacket=function(e,t){n(t.payload.image,function(r){t.payload.image=r,e.worker.postMessage(t)})}}).call(this,e("_process"))},{"../../package.json":3,_process:2}],5:[function(e,t){"use strict";t.exports=function(e){return e.paragraphs=[],e.lines=[],e.words=[],e.symbols=[],e.blocks.forEach(function(t){t.page=e,t.lines=[],t.words=[],t.symbols=[],t.paragraphs.forEach(function(r){r.block=t,r.page=e,r.words=[],r.symbols=[],r.lines.forEach(function(n){n.paragraph=r,n.block=t,n.page=e,n.symbols=[],n.words.forEach(function(o){o.line=n,o.paragraph=r,o.block=t,o.page=e,o.symbols.forEach(function(s){s.word=o,s.line=n,s.paragraph=r,s.block=t,s.page=e,s.line.symbols.push(s),s.paragraph.symbols.push(s),s.block.symbols.push(s),s.page.symbols.push(s)}),o.paragraph.words.push(o),o.block.words.push(o),o.page.words.push(o)}),n.block.lines.push(n),n.page.lines.push(n)}),r.page.paragraphs.push(r)})}),e}},{}],6:[function(e,t){"use strict";function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}var n=function(){function e(e,t){for(var r=0;r<t.length;r++){var n=t[r];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,r,n){return r&&e(t.prototype,r),n&&e(t,n),t}}(),o=e("../node/index.js"),s=0;t.exports=function(){function e(t){r(this,e),this.id="Job-"+ ++s+"-"+Math.random().toString(16).slice(3,8),this._instance=t,this._resolve=[],this._reject=[],this._progress=[],this._finally=[]}return n(e,[{key:"then",value:function(e,t){return this._resolve.push?this._resolve.push(e):e(this._resolve),t&&this.catch(t),this}},{key:"catch",value:function(e){return this._reject.push?this._reject.push(e):e(this._reject),this}},{key:"progress",value:function(e){return this._progress.push(e),this}},{key:"finally",value:function(e){return this._finally.push(e),this}},{key:"_send",value:function(e,t){o.sendPacket(this._instance,{jobId:this.id,action:e,payload:t})}},{key:"_handle",value:function(e){var t=e.data,r=!1;"resolve"===e.status?(0===this._resolve.length&&console.log(t),this._resolve.forEach(function(e){var r=e(t);r&&"function"==typeof r.then&&console.warn("TesseractJob instances do not chain like ES6 Promises. To convert it into a real promise, use Promise.resolve.")}),this._resolve=t,this._instance._dequeue(),r=!0):"reject"===e.status?(0===this._reject.length&&console.error(t),this._reject.forEach(function(e){return e(t)}),this._reject=t,this._instance._dequeue(),r=!0):"progress"===e.status?this._progress.forEach(function(e){return e(t)}):console.warn("Message type unknown",e.status),r&&this._finally.forEach(function(e){return e(t)})}}]),e}()},{"../node/index.js":4}],7:[function(e,t){"use strict";function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function n(e){e=e||{};var t=new l(c({},s.defaultOptions,e));return t.create=n,t.version=u,t}var o=function(){function e(e,t){for(var r=0;r<t.length;r++){var n=t[r];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,r,n){return r&&e(t.prototype,r),n&&e(t,n),t}}(),s=e("./node/index.js"),i=e("./common/circularize.js"),a=e("./common/job"),c=e("object-assign"),u=e("../package.json").version,l=function(){function e(t){r(this,e),this.worker=null,this.workerOptions=t,this._currentJob=null,this._queue=[]}return o(e,[{key:"recognize",value:function(e,t){var r=this;return this._delay(function(n){"string"==typeof t?t={lang:t}:(t=t||{},t.lang=t.lang||"eng"),n._send("recognize",{image:e,options:t,workerOptions:r.workerOptions})})}},{key:"detect",value:function(e,t){var r=this;return t=t||{},this._delay(function(n){n._send("detect",{image:e,options:t,workerOptions:r.workerOptions})})}},{key:"terminate",value:function(){this.worker&&s.terminateWorker(this),this.worker=null}},{key:"_delay",value:function(e){var t=this;this.worker||(this.worker=s.spawnWorker(this,this.workerOptions));var r=new a(this);return this._queue.push(function(){t._queue.shift(),t._currentJob=r,e(r)}),this._currentJob||this._dequeue(),r}},{key:"_dequeue",value:function(){this._currentJob=null,this._queue.length>0&&this._queue[0]()}},{key:"_recv",value:function(e){"resolve"===e.status&&"recognize"===e.action&&(e.data=i(e.data)),this._currentJob.id===e.jobId?this._currentJob._handle(e):console.warn("Job ID "+e.jobId+" not known.")}}]),e}(),f=n();t.exports=f},{"../package.json":3,"./common/circularize.js":5,"./common/job":6,"./node/index.js":4,"object-assign":1}]},{},[7])(7)});
	(function(e,f){var b={},g=function(a){b[a]&&(f.clearInterval(b[a]),b[a]=null)};e.fn.waitUntilExists=function(a,h,j){var c=this.selector,d=e(c),k=d.not(function(){return e(this).data("waitUntilExists.found")});"remove"===a?g(c):(k.each(a).data("waitUntilExists.found",!0),h&&d.length?g(c):j||(b[c]=f.setInterval(function(){d.waitUntilExists(a,h,!0)},500)));return d}})(jQuery,window);
	function getIdx(numbers, search){
		for (var i = 0; i < numbers.length; i++){
			if (numbers[i] == search)
				return i;
		}
		return -1;
	}
	function correctRecognition(ch){
		if (ch == 'O')
			return '0';
		else if (ch == 'Z')
			return '2';
		else if (ch == '’' || ch == "'")
			return '1';
		return ch;
	}
	function resolveExistingCaptcha(callback){
		var tmp = [
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(0)},
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(1)},
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(2)},
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(3)},
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(4)},
			{selector: $('#captcha-faucet > div > div').eq(1).find('div').eq(5)}
		];
		var tmp1 = tmp.map(function (item) {
			return (
					item.selector.find('img').attr('src')
			)
		});
		var calls = [];
		var numbers = [];
		tmp1.map(function(item, idx) {
			return (
					calls.push(Tesseract.recognize(item))
			)
		});
		var first = Promise.all(calls).then(result => {
			numbers = result.map(function (item) {
				return (correctRecognition(item.words[0].text[0]))
			});
			console.log('recognizing buttons done ! Buttons are : ', numbers);
		});

		var srcs = [];
		var toFill = [];
		var select = $('#captcha-faucet > div > div').eq(0).find('div').eq(0).find('div').eq(0).find('div').each(function(idx, val){
			srcs.push($(val).find('img').attr('src'));
		});
		var calls1 = srcs.map(function(item){
			return (Tesseract.recognize(item));
		});
		var second = Promise.all(calls1).then(result => {
			toFill = result.map(function(item){
				return (correctRecognition(item.words[0].text[0]))
			});
			console.log('recognizing numbers done ! Numbers are :', toFill);
		});

		console.log('Start recognition. please wait...');
		Promise.all([first, second]).then(r => {
			console.log('Got needed information :)');
			toFill.map(function(item){
				const idx = getIdx(numbers, item);
				tmp[idx].selector.click();
			});
			callback();
		});
	}
	function getFaucet(){
		$('#menu-left-faucet').click();
		$('#captcha-faucet-bot input').click();
		$('#captcha-faucet > div').waitUntilExists(function () {
			resolveExistingCaptcha(function(){
				$('#btn-get-faucet').click();
				console.log('Enjoy faucets !');
			});
		}, true);
	}
</script>
</BODY>
