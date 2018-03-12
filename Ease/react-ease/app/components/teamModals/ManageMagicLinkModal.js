import React from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Form, Button, Label, Icon, Segment, Container, Loader} from 'semantic-ui-react';
import {copyTextToClipboard} from "../../utils/utils";
import {connect} from "react-redux";
import {renewMagicLink} from "../../actions/magicLinkActions";
import {showManageMagicLinkModal} from "../../actions/teamModalActions";

@connect(store => ({
  modal: store.teamModals.manageMagicLinkModal
}))
class ManageMagicLinkModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      copied: false,
      time: null,
      timestamp: null,
      renewLink: false
    }
  }
  componentWillMount() {
    this.getTime();
  }
  renewLink = () => {
    this.setState({renewLink: true});
    this.props.dispatch(renewMagicLink({
      team_id: this.props.modal.team_card.team_id,
      team_card_id: this.props.modal.team_card.id
    })).then(response => {
      this.getTime();
      setTimeout(() => {
        this.setState({renewLink: false});
      }, 1000);
    });
  };
  copyPassword = () => {
    copyTextToClipboard(this.props.modal.team_card.magic_link);
    this.setState({copied: true});
    setTimeout(() => {
      this.setState({copied: false});
    }, 2000);
  };
  getTime = () => {
    if (this.props.modal.team_card.magic_link !== '' && this.props.modal.team_card.magic_link_expiration_date > this.state.timestamp) {
      setTimeout(() => {
        this.getTime();
      }, 1000);
      this.setState({time: new Date(), timestamp: new Date().getTime()});
    }
    else
      this.setState({time: null, timestamp: null});
  };
  close = () => {
    this.props.dispatch(showManageMagicLinkModal({active: false}));
  };
  render() {
    const {team_card} = this.props.modal;
    const hours = new Date(team_card.magic_link_expiration_date - this.state.time).getHours();
    const minutes = new Date(team_card.magic_link_expiration_date - this.state.time).getMinutes();
    const seconds = new Date(team_card.magic_link_expiration_date - this.state.time).getSeconds();
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={'Manage request link'}>
        <Container id="popup_team_single_card">
          <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
            <div className="squared_image_handler">
              <img src={team_card.website.logo} alt="Website logo"/>
            </div>
            <span className="app_name">{team_card.name}</span>
          </div>
          <Segment.Group>
            <Segment className='popup_magic_link'>
              <p style={{color: '#f5a623',fontSize:'16px'}}><Icon name='user'/> Status: waiting for login and password</p>
            </Segment>
            <Segment className='popup_magic_link'>
              <p style={{fontSize:'16px'}}>
                <Icon style={{color: '#45c997'}} name='check circle'/>
                Link valid
                for {hours <= 0 ? 23 + hours : hours - 1}:
                {minutes < 10 && minutes > -10 ? '0' : null}{minutes < 0 ? 60 + minutes : minutes}:
                {seconds < 10 && seconds > -10 ? '0' : null}{seconds < 0 ? 60 + seconds : seconds}
              </p>
            </Segment>
          </Segment.Group>
          <Form onSubmit={this.close}>
            {(!this.state.copied && !this.state.renewLink) &&
            <Button as='div' labelPosition='right' size='mini' onClick={this.copyPassword}>
              <Button type='button' icon style={{
                width: 'max-content',
                fontSize: '14px',
                backgroundColor: '#45c997',
                color: 'white',
                fontWeight: '300'
              }}>
                Copy link <Icon name='copy'/>
              </Button>
              <Label as='button' type='button' basic
                     style={{
                       fontWeight: '300',
                       width: '270px',
                       textOverflow: 'ellipsis',
                       overflow: 'hidden',
                       whiteSpace: 'nowrap',
                       display: 'block',
                       borderColor: '#45c997'
                     }}>
                {team_card.magic_link}
              </Label>
            </Button>}
            {(this.state.copied && !this.state.renewLink) &&
            <Segment
              size='mini'
              className='magic_link'
              content={'Copied!'}/>}
            {this.state.renewLink &&
            <Segment
              size='mini'
              className='magic_link'
              content={<Loader size='tiny' active inverted style={{width:'30px'}}/>}/>}
              <p style={{fontSize:'14px', color:'#949eb7'}}>
                <Icon name='refresh'/>
                <u onClick={this.renewLink} style={{cursor:'pointer'}}>Generate new link</u> (previous link will expire)
              </p>
            <Button
              type="submit"
              positive
              className="modal-button uppercase"
              content={`DONE`}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default ManageMagicLinkModal;