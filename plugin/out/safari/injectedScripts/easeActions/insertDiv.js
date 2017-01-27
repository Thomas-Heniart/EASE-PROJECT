var extensionId;
extension.getStorage("extensionId", function (eId) {
    extensionId = eId;
    $(document).ready(function(){
        $('body').prepend('<div id="ease_extension" extensionId="'+ eId +'" safariversion="2.0.1" style="dislay:none;">');
    });
});