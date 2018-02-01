<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<style>
    /* The switch - the box around the slider */
    .switch {
        position: relative;
        display: inline-block;
        width: 35px;
        height: 20px;
    }

    /* Hide default HTML checkbox */
    .switch input {
        display: none;
    }

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .slider:before {
        position: absolute;
        content: "";
        height: 12px;
        width: 12px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked + .slider {
        background-color: #45C997;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #45C997;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(15px);
        -ms-transform: translateX(15px);
        transform: translateX(15px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }
</style>
<div style="display: flex; margin-top: 6px; margin-left: 10px;">
    <strong style="margin-right: 18px;">Homepage</strong>
    <label class="switch">
        <input type="checkbox" id="homePageSwitch"/>
        <span class="slider round"></span>
    </label>
</div>
<div style="display: flex; margin-top: 0; margin-left: 17px; font-size: 12px;">Set up Ease as your browser homepage to
    access your web easily.
</div>
<div style="display: flex; margin-top: 6px; margin-left: 10px;">
    <strong style="margin-right: 10px;">Background</strong>
    <label class="switch">
        <input type="checkbox" id="backgroundSwitch"/>
        <span class="slider round"></span>
    </label>
</div>
<div style="display: flex; margin-top: 0; margin-left: 17px; font-size: 12px;">Would you like a background photo on your
    space every day?
</div>
<script>
    $(document).ready(function () {
        document.dispatchEvent(new CustomEvent("GetSettings", null));
        document.addEventListener("GetSettingsDone", function (event) {
            $("#homePageSwitch").prop("checked", event.detail);
        });
        if ($("body").hasClass("picBckgrnd")) {
            $('#backgroundSwitch').prop("checked", true);
        } else {
            $('#backgroundSwitch').prop("checked", false);
        }
        $("#backgroundSwitch").change(function () {
            if ($("body").hasClass("picBckgrnd")) {
                $("body").switchClass("picBckgrnd", "logoBckgrnd");
            } else if ($("body").hasClass("logoBckgrnd")) {
                $("body").switchClass("logoBckgrnd", "picBckgrnd");
            }
            var self = $(this);
            postHandler.post(
                'changeUserBackground',
                {},
                function () {
                },
                function (retMsg) {
                    easeTracker.trackEvent("DailyPhotoSwitch");
                    easeTracker.setDailyPhoto(self.is("checked"));
                },
                function (retMsg) {
                },
                "text"
            );
        });
    });
</script>