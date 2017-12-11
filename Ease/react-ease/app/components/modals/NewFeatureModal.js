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
            <p>Happy to see you here!</p>
          </Header>
          <div className='popup_content'>
            <p className='no_margin no_inline'>In the past few weeks we have been working to improve our product. 🙂</p>
            <p className='no_margin no_inline'><Icon name='heart'/> <strong>For your eyes only:</strong></p>
            <p>1.
              <img src='/resources/images/personal_space.png'/>
              <span>We changed the <strong>Personal Space</strong> to enhance a modern design for your daily usage.</span>
            </p>
            <p>2.
              <img src='/resources/images/catalog.png'/>
              <span>A fullscreen display of the <strong>Apps Catalog</strong> is available since a few weeks if you haven’t noticed it yet <Icon name='gift'/> </span></p>
            <p>3.
              <img className='phone' src='/resources/images/grey_phone.png'/>
              <span> Access your passwords from anywhere is now possible with our <strong>mobile app</strong> available on <strong>Androïd and iOS</strong>! Search Ease.space to download it on your smartphone.</span>
            </p>
            <p>4.
              <span>New settings are available, through the Top bar <Icon name='dropdown'/> to help you easily activate or deactivate your Daily <strong>background picture</strong>, Ease.space as <strong>homepage</strong>, and some other specificities.</span>
            </p>
            <p className='no_margin'><Icon name='users'/> <strong>For your team:</strong></p>
            <p>1. <span>To <strong>send Apps</strong> easily to your team, it works now from your Apps Catalog, just click on it!</span></p>
            <p>2. <span>Create your users in advance and <strong>send them invitations later</strong> once your platform is fully setup! Also you can <strong>re-send invitations</strong> when they forgot to join! It happens in user settings.</span></p>
            <p>3. <span><strong>Inviting new users</strong> is now easier! Send your invitation, then the user accepts and its done <Icon name='rocket'/></span></p>
            <p>4. <img style={{width: '60px', height: '65px', margin: '0 12px 0 0'}} src='/resources/images/new_app.png'/> <span><strong>Receiving apps</strong> is now more fluid. If a teammate sent you an app you will directly receive it in your Personal Space.</span></p>
            <p className='no_margin'>
              <span><strong>The whole Ease.space team really loves receiving your <a href='mailto:victor@ease.space' target="_top" style={{color: 'black'}}><u>feedbacks</u></a>, this is the only way to make this tool becomes the one you really need!</strong></span>
              <Button style={{width: '300px', padding: '0'}} positive content='Okay cheers!' onClick={this.close}/>
            </p>
          </div>
        </div>
      </div>
    )
  }
}

export default NewFeatureModal;