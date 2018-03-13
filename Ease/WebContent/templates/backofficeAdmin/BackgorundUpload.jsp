<h1>Upload new background</h1>
<form id="background-upload" class="ui form" method="POST" action="/api/v1/admin/UploadBackground"
      enctype="multipart/form-data">
    <div class="field">
        <label>Monday</label>
        <input type="file" name="2" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Tueday</label>
        <input type="file" name="3" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Wednesday</label>
        <input type="file" name="4" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Thursday</label>
        <input type="file" name="5" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Friday</label>
        <input type="file" name="6" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Saturday</label>
        <input type="file" name="7" accept="image/jpeg"/>
    </div>
    <div class="field">
        <label>Sunday</label>
        <input type="file" name="1" accept="image/jpeg"/>
    </div>
    <button class="ui blue button disabled">Submit</button>
</form>