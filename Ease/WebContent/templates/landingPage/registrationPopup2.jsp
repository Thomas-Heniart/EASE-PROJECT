<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="popupHandler">
    <div class="overlay"></div>
    <div class="easePopup show" id="signUpPopup">
        <div class="title">
            <p>
                <fmt:message key="landing.signup-popup.title"/>
            </p>
        </div>
        <div class="bodysHandler">
            <div class="popupBody" id="3">
                <form class="handler" action="/api/v1/common/CheckRegistrationDigits">
                    <div class="row">
                        <p class="row-heading">Enter digits</p>
                    </div>
                    <div class="row">
                        <input name="digits"/>
                    </div>
                    <div class="row text-center">
                        <img class="loading" src="resources/other/facebook-loading.svg"/>
                        <button class="btn submitButton" type="submit">
                            <fmt:message key="landing.signup-popup.page-2.button"/>
                        </button>
                        <p class="alert-message"></p>
                    </div>
                </form>
            </div>
            <div class="popupBody" id="2">
                <form class="handler"
                      action='/api/v1/common/Registration${param.school == null ? '' : param.school}'>
                    <div class="row">
                        <p class="row-heading">
                            <fmt:message key="landing.signup-popup.page-2.password-title"/>
                        </p>
                        <div class="infoText">
                            <p>
                                <span>Info:</span> <fmt:message key="landing.signup-popup.page-2.password-info"/>
                            </p>
                        </div>
                    </div>
                    <div class="row">
                        <span class="input">
							<input type="password" name="password" placeholder=
                                    <fmt:message key="landing.signup-popup.page-2.password-placeholder"/>/>
							<div class="showPassDiv">
								<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
								<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
							</div>
							<div id="validatorPassword" class="passwordValidator" style="display:none">
								<i class="fa fa-times error" aria-hidden="true" style="color:#d75a4a;"></i>
								<i class="fa fa-check success" aria-hidden="true" style="color:#24d666;"></i>
							</div>
							<img src="./resources/icons/error.png" id="validatorPassword" style="display:none;"/>
						</span>
                        <div class="progress">
                            <div class="progress-bar">
                            </div>
                        </div>
                    </div>
                    <div class="row">
						<span class="input">
							<input type="password" name="confirmPassword" placeholder=
                                    <fmt:message key="landing.signup-popup.page-2.password-confirm-placeholder"/>/>
							<div class="showPassDiv">
								<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
								<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
							</div>
							<div id="validatorConfirmPass" class="passwordValidator" style="display:none">
								<i class="fa fa-times error" aria-hidden="true" style="color:#d75a4a;"></i>
								<i class="fa fa-check success" aria-hidden="true" style="color:#24d666;"></i>
							</div>
						</span>
                    </div>
                    <div class="row text-center">
                        <img class="loading" src="resources/other/facebook-loading.svg"/>
                        <button class="btn submitButton" type="submit">
                            <fmt:message key="landing.signup-popup.page-2.button"/>
                        </button>
                        <p class="alert-message"></p>
                    </div>
                </form>
            </div>
            <div class="popupBody show" id="1">
                <form class="handler"
                      action='/api/v1/common/AskRegistration${param.school == null ? '' : param.school}'>
                    <div class="row">
                        <p class="row-heading">
                            <fmt:message key="landing.signup-popup.page-2.name-title"/>
                        </p>
                        <input type="text" name="name" placeholder="Name..."/>
                    </div>
                    <div class="row">
                        <p class="row-heading">
                            <fmt:message key="landing.signup-popup.email-title"/>
                        </p>
                        <input type="email" name="email" placeholder="@something..."/>
                    </div>
                    <div class="row terms">
                        <p><fmt:message key="landing.signup-popup.page-1.terms-accept"/> <a href="privacy"
                                                                                            target="_blank"><fmt:message
                                key="landing.signup-popup.page-1.terms"/></a></p>
                    </div>
                    <div class="row text-center">
                        <img class="loading" src="resources/other/facebook-loading.svg"/>
                        <button class="btn submitButton" type="submit">
                            <fmt:message key="landing.signup-popup.page-1.button"/>
                        </button>
                        <p class="alert-message"></p>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
