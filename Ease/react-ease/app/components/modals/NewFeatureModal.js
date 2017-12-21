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
            <p>🎄☃️Christmas features!🎁🎅</p>
          </Header>
          <div className='popup_content'>
            <p className='no_margin no_inline'>Lately we worked on making any websites and softwares’ passwords storable on Ease.space.</p>
            <p className='no_margin no_inline'><Icon name='heart'/> <strong>Here is what we’ve built for you</strong></p>
            <p>1.
              <img src='/resources/images/new_catalog.png'/>
              <span>The <strong>Apps Catalogue</strong> gets 2 main new features:<br/>- store passwords of <strong>any website</strong> you want! The connection won’t be automatic at first but, if needed you can instantly ask us to make it work<Icon name='rocket'/><br/>- store passwords of <strong>softwares</strong>!<Icon name='gift'/></span>
            </p>
            <p>2.
              <img src='/resources/images/popup_any_app.png'/>
              <span><strong>To access</strong> any website and softwares’ passwords, just click on the app as usual, then a <strong>little popup</strong> will appear and help you easily copy your login and password <Icon name='copy'/></span></p>
            <p>3.
              <img src='/resources/images/team_star_wars.png'/>
              <span>Of course any websites and softwares’ passwords are also <strong>shareable</strong> to your team! If you don’t have one yet, hit the <strong>Teams</strong> <Icon name='users'/>button!</span>
            </p>
            <p className='no_margin'>
              <span><strong>The whole Ease.space team really loves receiving your <a href='mailto:victor@ease.space' target="_top" style={{color: 'black'}}><u>feedback</u></a>, this is the only way to make this tool becomes the one you really need!</strong></span>
              <Button style={{width: '300px', padding: '0'}} positive content='Okay cheers!' onClick={this.close}/>
            </p>
          </div>
        </div>
      </div>
    )
  }
}

export default NewFeatureModal;