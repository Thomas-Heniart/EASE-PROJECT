function BigInteger(t,i,r){null!=t&&("number"==typeof t?this.fromNumber(t,i,r):null==i&&"string"!=typeof t?this.fromString(t,256):this.fromString(t,i))}function nbi(){return new BigInteger(null)}function am1(t,i,r,o,n,e){for(;--e>=0;){var s=i*this[t++]+r[o]+n;n=Math.floor(s/67108864),r[o++]=67108863&s}return n}function am2(t,i,r,o,n,e){for(var s=32767&i,h=i>>15;--e>=0;){var p=32767&this[t],a=this[t++]>>15,f=h*p+a*s;p=s*p+((32767&f)<<15)+r[o]+(1073741823&n),n=(p>>>30)+(f>>>15)+h*a+(n>>>30),r[o++]=1073741823&p}return n}function am3(t,i,r,o,n,e){for(var s=16383&i,h=i>>14;--e>=0;){var p=16383&this[t],a=this[t++]>>14,f=h*p+a*s;p=s*p+((16383&f)<<14)+r[o]+n,n=(p>>28)+(f>>14)+h*a,r[o++]=268435455&p}return n}function int2char(t){return BI_RM.charAt(t)}function intAt(t,i){var r=BI_RC[t.charCodeAt(i)];return null==r?-1:r}function bnpCopyTo(t){for(var i=this.t-1;i>=0;--i)t[i]=this[i];t.t=this.t,t.s=this.s}function bnpFromInt(t){this.t=1,this.s=0>t?-1:0,t>0?this[0]=t:-1>t?this[0]=t+this.DV:this.t=0}function nbv(t){var i=nbi();return i.fromInt(t),i}function bnpFromString(t,i){var r;if(16==i)r=4;else if(8==i)r=3;else if(256==i)r=8;else if(2==i)r=1;else if(32==i)r=5;else{if(4!=i)return void this.fromRadix(t,i);r=2}this.t=0,this.s=0;for(var o=t.length,n=!1,e=0;--o>=0;){var s=8==r?255&t[o]:intAt(t,o);0>s?"-"==t.charAt(o)&&(n=!0):(n=!1,0==e?this[this.t++]=s:e+r>this.DB?(this[this.t-1]|=(s&(1<<this.DB-e)-1)<<e,this[this.t++]=s>>this.DB-e):this[this.t-1]|=s<<e,e+=r,e>=this.DB&&(e-=this.DB))}8==r&&0!=(128&t[0])&&(this.s=-1,e>0&&(this[this.t-1]|=(1<<this.DB-e)-1<<e)),this.clamp(),n&&BigInteger.ZERO.subTo(this,this)}function bnpClamp(){for(var t=this.s&this.DM;this.t>0&&this[this.t-1]==t;)--this.t}function bnToString(t){if(this.s<0)return"-"+this.negate().toString(t);var i;if(16==t)i=4;else if(8==t)i=3;else if(2==t)i=1;else if(32==t)i=5;else{if(4!=t)return this.toRadix(t);i=2}var r,o=(1<<i)-1,n=!1,e="",s=this.t,h=this.DB-s*this.DB%i;if(s-->0)for(h<this.DB&&(r=this[s]>>h)>0&&(n=!0,e=int2char(r));s>=0;)i>h?(r=(this[s]&(1<<h)-1)<<i-h,r|=this[--s]>>(h+=this.DB-i)):(r=this[s]>>(h-=i)&o,0>=h&&(h+=this.DB,--s)),r>0&&(n=!0),n&&(e+=int2char(r));return n?e:"0"}function bnNegate(){var t=nbi();return BigInteger.ZERO.subTo(this,t),t}function bnAbs(){return this.s<0?this.negate():this}function bnCompareTo(t){var i=this.s-t.s;if(0!=i)return i;var r=this.t;if(i=r-t.t,0!=i)return this.s<0?-i:i;for(;--r>=0;)if(0!=(i=this[r]-t[r]))return i;return 0}function nbits(t){var i,r=1;return 0!=(i=t>>>16)&&(t=i,r+=16),0!=(i=t>>8)&&(t=i,r+=8),0!=(i=t>>4)&&(t=i,r+=4),0!=(i=t>>2)&&(t=i,r+=2),0!=(i=t>>1)&&(t=i,r+=1),r}function bnBitLength(){return this.t<=0?0:this.DB*(this.t-1)+nbits(this[this.t-1]^this.s&this.DM)}function bnpDLShiftTo(t,i){var r;for(r=this.t-1;r>=0;--r)i[r+t]=this[r];for(r=t-1;r>=0;--r)i[r]=0;i.t=this.t+t,i.s=this.s}function bnpDRShiftTo(t,i){for(var r=t;r<this.t;++r)i[r-t]=this[r];i.t=Math.max(this.t-t,0),i.s=this.s}function bnpLShiftTo(t,i){var r,o=t%this.DB,n=this.DB-o,e=(1<<n)-1,s=Math.floor(t/this.DB),h=this.s<<o&this.DM;for(r=this.t-1;r>=0;--r)i[r+s+1]=this[r]>>n|h,h=(this[r]&e)<<o;for(r=s-1;r>=0;--r)i[r]=0;i[s]=h,i.t=this.t+s+1,i.s=this.s,i.clamp()}function bnpRShiftTo(t,i){i.s=this.s;var r=Math.floor(t/this.DB);if(r>=this.t)return void(i.t=0);var o=t%this.DB,n=this.DB-o,e=(1<<o)-1;i[0]=this[r]>>o;for(var s=r+1;s<this.t;++s)i[s-r-1]|=(this[s]&e)<<n,i[s-r]=this[s]>>o;o>0&&(i[this.t-r-1]|=(this.s&e)<<n),i.t=this.t-r,i.clamp()}function bnpSubTo(t,i){for(var r=0,o=0,n=Math.min(t.t,this.t);n>r;)o+=this[r]-t[r],i[r++]=o&this.DM,o>>=this.DB;if(t.t<this.t){for(o-=t.s;r<this.t;)o+=this[r],i[r++]=o&this.DM,o>>=this.DB;o+=this.s}else{for(o+=this.s;r<t.t;)o-=t[r],i[r++]=o&this.DM,o>>=this.DB;o-=t.s}i.s=0>o?-1:0,-1>o?i[r++]=this.DV+o:o>0&&(i[r++]=o),i.t=r,i.clamp()}function bnpMultiplyTo(t,i){var r=this.abs(),o=t.abs(),n=r.t;for(i.t=n+o.t;--n>=0;)i[n]=0;for(n=0;n<o.t;++n)i[n+r.t]=r.am(0,o[n],i,n,0,r.t);i.s=0,i.clamp(),this.s!=t.s&&BigInteger.ZERO.subTo(i,i)}function bnpSquareTo(t){for(var i=this.abs(),r=t.t=2*i.t;--r>=0;)t[r]=0;for(r=0;r<i.t-1;++r){var o=i.am(r,i[r],t,2*r,0,1);(t[r+i.t]+=i.am(r+1,2*i[r],t,2*r+1,o,i.t-r-1))>=i.DV&&(t[r+i.t]-=i.DV,t[r+i.t+1]=1)}t.t>0&&(t[t.t-1]+=i.am(r,i[r],t,2*r,0,1)),t.s=0,t.clamp()}function bnpDivRemTo(t,i,r){var o=t.abs();if(!(o.t<=0)){var n=this.abs();if(n.t<o.t)return null!=i&&i.fromInt(0),void(null!=r&&this.copyTo(r));null==r&&(r=nbi());var e=nbi(),s=this.s,h=t.s,p=this.DB-nbits(o[o.t-1]);p>0?(o.lShiftTo(p,e),n.lShiftTo(p,r)):(o.copyTo(e),n.copyTo(r));var a=e.t,f=e[a-1];if(0!=f){var u=f*(1<<this.F1)+(a>1?e[a-2]>>this.F2:0),g=this.FV/u,m=(1<<this.F1)/u,c=1<<this.F2,v=r.t,b=v-a,l=null==i?nbi():i;for(e.dlShiftTo(b,l),r.compareTo(l)>=0&&(r[r.t++]=1,r.subTo(l,r)),BigInteger.ONE.dlShiftTo(a,l),l.subTo(e,e);e.t<a;)e[e.t++]=0;for(;--b>=0;){var T=r[--v]==f?this.DM:Math.floor(r[v]*g+(r[v-1]+c)*m);if((r[v]+=e.am(0,T,r,b,0,a))<T)for(e.dlShiftTo(b,l),r.subTo(l,r);r[v]<--T;)r.subTo(l,r)}null!=i&&(r.drShiftTo(a,i),s!=h&&BigInteger.ZERO.subTo(i,i)),r.t=a,r.clamp(),p>0&&r.rShiftTo(p,r),0>s&&BigInteger.ZERO.subTo(r,r)}}}function bnMod(t){var i=nbi();return this.abs().divRemTo(t,null,i),this.s<0&&i.compareTo(BigInteger.ZERO)>0&&t.subTo(i,i),i}function Classic(t){this.m=t}function cConvert(t){return t.s<0||t.compareTo(this.m)>=0?t.mod(this.m):t}function cRevert(t){return t}function cReduce(t){t.divRemTo(this.m,null,t)}function cMulTo(t,i,r){t.multiplyTo(i,r),this.reduce(r)}function cSqrTo(t,i){t.squareTo(i),this.reduce(i)}function bnpInvDigit(){if(this.t<1)return 0;var t=this[0];if(0==(1&t))return 0;var i=3&t;return i=i*(2-(15&t)*i)&15,i=i*(2-(255&t)*i)&255,i=i*(2-((65535&t)*i&65535))&65535,i=i*(2-t*i%this.DV)%this.DV,i>0?this.DV-i:-i}function Montgomery(t){this.m=t,this.mp=t.invDigit(),this.mpl=32767&this.mp,this.mph=this.mp>>15,this.um=(1<<t.DB-15)-1,this.mt2=2*t.t}function montConvert(t){var i=nbi();return t.abs().dlShiftTo(this.m.t,i),i.divRemTo(this.m,null,i),t.s<0&&i.compareTo(BigInteger.ZERO)>0&&this.m.subTo(i,i),i}function montRevert(t){var i=nbi();return t.copyTo(i),this.reduce(i),i}function montReduce(t){for(;t.t<=this.mt2;)t[t.t++]=0;for(var i=0;i<this.m.t;++i){var r=32767&t[i],o=r*this.mpl+((r*this.mph+(t[i]>>15)*this.mpl&this.um)<<15)&t.DM;for(r=i+this.m.t,t[r]+=this.m.am(0,o,t,i,0,this.m.t);t[r]>=t.DV;)t[r]-=t.DV,t[++r]++}t.clamp(),t.drShiftTo(this.m.t,t),t.compareTo(this.m)>=0&&t.subTo(this.m,t)}function montSqrTo(t,i){t.squareTo(i),this.reduce(i)}function montMulTo(t,i,r){t.multiplyTo(i,r),this.reduce(r)}function bnpIsEven(){return 0==(this.t>0?1&this[0]:this.s)}function bnpExp(t,i){if(t>4294967295||1>t)return BigInteger.ONE;var r=nbi(),o=nbi(),n=i.convert(this),e=nbits(t)-1;for(n.copyTo(r);--e>=0;)if(i.sqrTo(r,o),(t&1<<e)>0)i.mulTo(o,n,r);else{var s=r;r=o,o=s}return i.revert(r)}function bnModPowInt(t,i){var r;return r=256>t||i.isEven()?new Classic(i):new Montgomery(i),this.exp(t,r)}var dbits,canary=0xdeadbeefcafe,j_lm=15715070==(16777215&canary);j_lm&&"Microsoft Internet Explorer"==navigator.appName?(BigInteger.prototype.am=am2,dbits=30):j_lm&&"Netscape"!=navigator.appName?(BigInteger.prototype.am=am1,dbits=26):(BigInteger.prototype.am=am3,dbits=28),BigInteger.prototype.DB=dbits,BigInteger.prototype.DM=(1<<dbits)-1,BigInteger.prototype.DV=1<<dbits;var BI_FP=52;BigInteger.prototype.FV=Math.pow(2,BI_FP),BigInteger.prototype.F1=BI_FP-dbits,BigInteger.prototype.F2=2*dbits-BI_FP;var BI_RM="0123456789abcdefghijklmnopqrstuvwxyz",BI_RC=new Array,rr,vv;for(rr="0".charCodeAt(0),vv=0;9>=vv;++vv)BI_RC[rr++]=vv;for(rr="a".charCodeAt(0),vv=10;36>vv;++vv)BI_RC[rr++]=vv;for(rr="A".charCodeAt(0),vv=10;36>vv;++vv)BI_RC[rr++]=vv;Classic.prototype.convert=cConvert,Classic.prototype.revert=cRevert,Classic.prototype.reduce=cReduce,Classic.prototype.mulTo=cMulTo,Classic.prototype.sqrTo=cSqrTo,Montgomery.prototype.convert=montConvert,Montgomery.prototype.revert=montRevert,Montgomery.prototype.reduce=montReduce,Montgomery.prototype.mulTo=montMulTo,Montgomery.prototype.sqrTo=montSqrTo,BigInteger.prototype.copyTo=bnpCopyTo,BigInteger.prototype.fromInt=bnpFromInt,BigInteger.prototype.fromString=bnpFromString,BigInteger.prototype.clamp=bnpClamp,BigInteger.prototype.dlShiftTo=bnpDLShiftTo,BigInteger.prototype.drShiftTo=bnpDRShiftTo,BigInteger.prototype.lShiftTo=bnpLShiftTo,BigInteger.prototype.rShiftTo=bnpRShiftTo,BigInteger.prototype.subTo=bnpSubTo,BigInteger.prototype.multiplyTo=bnpMultiplyTo,BigInteger.prototype.squareTo=bnpSquareTo,BigInteger.prototype.divRemTo=bnpDivRemTo,BigInteger.prototype.invDigit=bnpInvDigit,BigInteger.prototype.isEven=bnpIsEven,BigInteger.prototype.exp=bnpExp,BigInteger.prototype.toString=bnToString,BigInteger.prototype.negate=bnNegate,BigInteger.prototype.abs=bnAbs,BigInteger.prototype.compareTo=bnCompareTo,BigInteger.prototype.bitLength=bnBitLength,BigInteger.prototype.mod=bnMod,BigInteger.prototype.modPowInt=bnModPowInt,BigInteger.ZERO=nbv(0),BigInteger.ONE=nbv(1);