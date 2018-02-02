import React from 'react';
import {Button, Icon} from 'semantic-ui-react';

class Explication extends React.Component {
  render() {
    const {
      next,
      back,
      passwordManager
    } = this.props;
    return (
      <React.Fragment>
        {passwordManager === 2 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Chrome.png"/> Import your passwords from Chrome</p>
          <p><a  target='_blank'/></p>
          <p>2. Once you get the CSV, click on Next bellow :)</p>
        </React.Fragment>}
        {passwordManager === 3 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Dashlane.png"/> Import your passwords from Dashlane</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Dashlane to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open your Dashlane desktop application.</li>
            <li>Click the “<strong>File</strong>” tab</li>
            <li>Select “<strong>Export</strong>”, then choose “<strong>Export to CSV</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 4 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Lastpass.png"/> Import your passwords from LastPass</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Lastpass to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open Lastpass, click on “<strong>More Options</strong>” > “<strong>Advanced</strong>” > “<strong>Export</strong>”.</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Copy the whole text containing your account names, logins and passwords.</li>
            <li>Click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 5 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/1password.png"/> Import your passwords from 1Password</p>
          <p className='no_margin'>&emsp;1. Export your passwords from 1Password to a <strong>CSV file</strong>.</p>
          <ul>
            <li>Open 1Password desktop application</li>
            <li>Select a Vault (you cannot export several Vaults at once)</li>
            <li>Click the “<strong>File</strong>” tab > “<strong>Export</strong>” > “<strong>All elements</strong>”</li>
            <li>Enter your 1Password master password</li>
            <li>As File Format chose <strong>Comma Separated Values (.csv)</strong></li>
            <li>Leave other default settings as displayed, then click “<strong>Save</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 6 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Keepass.png"/> Import your passwords from Keepass</p>
          <p>&emsp;1. Export your passwords from Keepass to a <strong>CSV file</strong>.
            <a href='https://keepass.info/help/base/importexport.html' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 7 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/roboform.png"/> Import your passwords from RoboForm</p>
          <p>&emsp;1. Export your passwords from Roboform to a <strong>CSV file</strong>.
            <a href='https://help.roboform.com/hc/en-us/articles/230425008-How-to-export-your-RoboForm-logins-' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 8 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/Zohovault.png"/> Import your passwords from Zoho Vault</p>
          <p className='no_margin'>&emsp;1. Export your passwords from Zoho Vault</p>
          <ul>
            <li>Open Zoho Vault.</li>
            <li>Click the “<strong>Tools</strong>” tab. Choose “<strong>Export Secrets</strong>” button on the left panel.</li>
            <li>You can either export all the secrets OR export only the ones that belong to a particular secret type.</li>
            <li>Select “<strong>General CSV</strong>”</li>
            <li>Click on “<strong>Export Secrets</strong>”</li>
          </ul>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Copy the whole text containing your account names, logins and passwords.</li>
            <li>Click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        {passwordManager === 9 &&
        <React.Fragment>
          <p className='title'><img src="/resources/other/passpack.png"/> Import your passwords from Passpack</p>
          <p>&emsp;1. Export your passwords from Passpack to a <strong>Comma Separated Values (CSV) file</strong>.
            <a href='https://help.roboform.com/hc/en-us/articles/230425008-How-to-export-your-RoboForm-logins-' target='_blank'>
              Check how here
            </a>
          </p>
          <p className='no_margin'>&emsp;2. Your almost done!</p>
          <ul>
            <li>Open the downloaded file.</li>
            <li>Copy the whole content of the file, then click on “<strong>Next</strong>” bellow :)</li>
          </ul>
        </React.Fragment>}
        <Button className={'left'} onClick={back}>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

export default Explication;