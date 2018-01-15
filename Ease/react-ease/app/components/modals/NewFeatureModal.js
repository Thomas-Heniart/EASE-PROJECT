import React from 'react';
import {Header, Icon, Button} from 'semantic-ui-react';
import {showNewFeatureModal} from "../../actions/modalActions";
import {connect} from "react-redux";
import {newFeatureSeen} from "../../actions/commonActions";

@connect(store => ({
  modal: store.modals,
}))
class NewFeatureModal extends React.Component {
  constructor(props) {
    super(props);
  }
  close = () => {
    this.props.dispatch(newFeatureSeen());
    this.props.dispatch(showNewFeatureModal({active: false}));
  };
  render() {
    return (
      <div className="popupHandler myshow">
        <div className="popover_mask"/>
        <div className="ease_popup ease_new_feature_popup">
          <Header as="h3" attached="top">
            <p>⛷✨Smooth password importation tool🌬🔐</p>
          </Header>
          <div className='popup_content'>
            <p className='no_margin no_inline'>Many of you helped us understand how painfull it was to type all your personal or team'passwords one by one on Ease.space.</p>
            <p className='no_margin no_inline' style={}><Icon name='rocket'/> <strong>We made password importation... powerfull.</strong></p>
            <p>1.
              <img src='/resources/images/add_new_app.png'/>
              <span>Go through the <strong>Add new App</strong> button</span>
            </p>
            <p>2.
              <img src='/resources/images/import_tab.png'/>
              <span>Click the <strong>"<Icon name="cloud upload"/>Import" tab.</strong> Choose your way to import your passwords. From Excel, to 8 different password managers, you can integrate your apps all at once!</span>
            </p>
            <p>3.
              <img src='/resources/images/plane_launch.png'/>
              <span>You will <strong>take the control</strong> of all your personal and team passwords smoothly.</span>
            </p>
            <div style={{display: 'flex', position: 'relative'}}>
              <p className='no_margin'>
              <span>
                <strong>If you wanna try and give us your <a href='mailto:victor@ease.space' target="_top" style={{color: 'black'}}><u>feedbacks</u></a>, it helps us a lot <Icon name="heart"/></strong><br/>
                <strong>If you love Ease.space, share it with us on <a href='https://twitter.com/intent/tweet?text=%40ease_space%20%23thiseaselove' target="_blank" style={{color: 'black'}}><u>Twitter</u></a> <Icon name="heart"/></strong>
              </span>
              </p>
              <Button style={{width: '240px', padding: '0', position: 'absolute', height: '48px', right: '0', bottom: '5px'}} positive content='Okay cheers!' onClick={this.close}/>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default NewFeatureModal;