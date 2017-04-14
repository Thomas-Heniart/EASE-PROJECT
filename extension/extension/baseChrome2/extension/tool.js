var tool = {
	generateSingleId:function() {
		var date = new Date();
		var components = [
    		date.getYear(),
    		date.getMonth(),
    		date.getDate(),
    		date.getHours(),
    		date.getMinutes(),
    		date.getSeconds(),
    		date.getMilliseconds()
		];
		return components.join("");
	},
    getCode:function(filePath, callback) {
        var tab = filePath.split(".");
        chrome.storage.local.get("updater", function(item) {
            var thing = item.updater.files;
            while (tab.length > 0) {
                thing = thing[tab[0]];
                tab.shift();
            }
            callback(thing);
        });
    },
    sizeOf:function ( object ) {

        var objectList = [];
        var stack = [ object ];
        var bytes = 0;

        while ( stack.length ) {
            var value = stack.pop();

            if ( typeof value === 'boolean' ) {
                bytes += 4;
            }
            else if ( typeof value === 'string' ) {
                bytes += value.length * 2;
            }
            else if ( typeof value === 'number' ) {
                bytes += 8;
            }
            else if
                (
                    typeof value === 'object'
                    && objectList.indexOf( value ) === -1
                    )
            {
                objectList.push( value );

                for( var i in value ) {
                    stack.push( value[ i ] );
                }
            }
        }
        return bytes;
    }
}