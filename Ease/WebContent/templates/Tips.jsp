<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="tipsHandler">
    <div class="tip display-flex flex_direction_column" id="0" step="1">
        <h3>This is your Space and your Apps</h3>
        <span class="content">You'll be able to add more later. Apps are the quick & easy way to connect to websites. Just click on one and let us take care of everything.</span>
        <div class="buttonHandler">
            <div class="skip">
                <span>Already know how Ease.space works?</span>
                <span class="action">Skip tutorial</span>
            </div>
            <button class="btn" type="submit">Got it</button>
        </div>
    </div>
    <c:choose>
        <c:when test="${user.isSchoolUser()}">
            <div class="tip" id="1" step="2">
                <div class="arrow" style="right: 15px;"></div>
                <h3>Find more apps in our catalog</h3>
                <div class="buttonHandler">
                    <div class="skip">
                        <span>Already know how Ease.space works?</span>
                        <span class="action">Skip tutorial</span>
                    </div>
                    <button class="btn" type="submit">Got it</button>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- <div class="tip" id="1" step="2">
                <div class="arrow" style="right: 101px"></div>
                <h3>If you need create your team here</h3>
                <div class="buttonHandler">
                    <div class="skip">
                        <span>Already know how Ease.space works?</span>
                        <span class="action">Skip tutorial</span>
                    </div>
                    <button class="btn" type="submit">Got it</button>
                </div>
            </div> -->
            <div class="tip" id="1" step="2">
                <div class="arrow" style="right: 15px;"></div>
                <h3>Find more apps in our catalog</h3>
                <div class="buttonHandler">
                    <div class="skip">
                        <span>Already know how Ease.space works?</span>
                        <span class="action">Skip tutorial</span>
                    </div>
                    <button class="btn" type="submit">Got it</button>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script type="text/javascript">
    window.addEventListener('load', function () {
        $('#tipsHandler .tip#0').addClass('show');
        $('#tipsHandler .tip#0 button').click(() => {
            $('#tipsHandler .tip#0').removeClass('show');
            $('#tipsHandler .tip#1').addClass('show');
        });
        $('#tipsHandler .tip .action').click(e => {
            postHandler.post('/api/v1/common/TutoDone', {}, function () {
                $('#tipsHandler').remove();
            }, function (retMsg) {
                $('#tipsHandler').remove();
            }, function (retMsg) {
                //error
            }, 'text');
        });
        $('#tipsHandler .tip#1 button').click(() => {
            var tip1 = $('#tipsHandler .tip#1');
            tip1.removeClass('show');
            var tip2 = $("#tipsHandler .tip#2");
            if (tip2.length > 0) {
                tip2.addClass('show');
                tip2.click(function (e) {
                    var self = $(this).parent().parent();
                    var step = self.attr("step");
                    postHandler.post('/api/v1/common/TutoDone', {}, function () {
                        tip2.removeClass('show');
                        $('#tipsHandler').remove();
                    }, function (retMsg) {
                        //succes
                        self.remove();
                    }, function (retMsg) {
                        //error
                    }, 'text');
                });
            } else {
                postHandler.post('/api/v1/common/TutoDone', {}, function () {
                    $('#tipsHandler').remove();
                }, function (retMsg) {
                    //succes
                    self.remove();
                }, function (retMsg) {
                    //error
                }, 'text');
            }

        });
    });
</script>