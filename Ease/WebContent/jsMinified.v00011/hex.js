!function(r){"use strict";var t,e={};e.decode=function(e){var a;if(t===r){var o="0123456789ABCDEF",f=" \f\n\r	 \u2028\u2029";for(t=[],a=0;16>a;++a)t[o.charAt(a)]=a;for(o=o.toLowerCase(),a=10;16>a;++a)t[o.charAt(a)]=a;for(a=0;a<f.length;++a)t[f.charAt(a)]=-1}var i=[],n=0,c=0;for(a=0;a<e.length;++a){var h=e.charAt(a);if("="==h)break;if(h=t[h],-1!=h){if(h===r)throw"Illegal character at offset "+a;n|=h,++c>=2?(i[i.length]=n,n=0,c=0):n<<=4}}if(c)throw"Hex encoding incomplete: 4 bits missing";return i},window.Hex=e}();