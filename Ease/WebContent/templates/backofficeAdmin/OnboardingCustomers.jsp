<table class="ui compact small selectable sortable celled table" id="onboardingCustomersTable">
    <thead>
    <tr>
        <th>count</th>
        <th>company</th>
        <th>size</th>
        <th>email</th>
        <th>full name</th>
        <th>phone number</th>
        <th>date</th>
        <th>action</th>
    </tr>
    </thead>
    <tbody id="onboardingCustomersTableBody"></tbody>
</table>
<div class="ui modal" id="onboardingCustomerEmailModal">
    <i class="close icon"></i>
    <div class="header">Email to: </div>
    <div class="content">
        <form action="/api/v1/admin/onboarding/transfer-ownership" class="ui form">
            <div class="field">
                <label>Email first part: </label>
                <textarea name="firstPart" placeholder="Type beginning of your email here..."></textarea>
            </div>
            <div class="field">
                <label>Link appearance</label>
                <input type="text" name="linkName" placeholder="Ex: Click here to register...">
            </div>
            <div class="field">
                <label>Email last part: </label>
                <textarea name="lastPart" placeholder="Type end of your email here..."></textarea>
            </div>
            <button class="ui button primary">Submit</button>
        </form>
    </div>
</div>
<script src="js/backOffice/onboardingCustomers.js?v=3" async></script>