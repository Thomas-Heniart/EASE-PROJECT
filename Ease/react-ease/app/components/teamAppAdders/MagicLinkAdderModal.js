import React from 'react';
import {copyTextToClipboard} from "../../utils/utils";
import { Form, Button, Label, Icon, Segment, Container } from 'semantic-ui-react';

class MagicLinkAdderModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      copied: false,
      disabled: true
    }
  }
  copyPassword = () => {
    this.setState({disabled: false});
    copyTextToClipboard(this.props.link);
    this.setState({copied: true});
    setTimeout(() => {
      this.setState({copied: false});
    }, 2000);
  };
  render() {
    const {
      link,
      website,
      appName,
      loading,
      confirm
    } = this.props;
    return (
      <Container id="popup_team_single_card">
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <img src={website.logo} alt="Website logo"/>
          </div>
          <span className="app_name">{appName}</span>
        </div>
        <div>
          <p>Send this link to ask the login and password</p>
        </div>
        <Form onSubmit={confirm}>
          {!this.state.copied &&
          <Button as='div' labelPosition='right' size='mini' onClick={this.copyPassword} style={{margin:'10px 0 0 0'}}>
            <Button type='button' icon style={{width:'max-content',fontSize:'14px',backgroundColor:'#45c997',color:'white',fontWeight:'300'}}>
              Copy link <Icon name='copy' />
            </Button>
            <Label as='button' type='button' basic
                   style={{fontWeight:'300',width:'270px',textOverflow:'ellipsis',overflow:'hidden',whiteSpace:'nowrap',display:'block',borderColor:'#45c997'}}>
              {link}
            </Label>
          </Button>}
          {this.state.copied &&
          <Segment
            size='mini'
            className='magic_link'
            content={'Copied!'}/>}
          <p style={{fontSize:'14px',color:'#949eb7'}}>The link will be valid until request is answered, or for 24 hours maximum.</p>
          <Button
            type="submit"
            loading={loading}
            disabled={loading || this.state.disabled}
            positive
            className="modal-button uppercase"
            content={`DONE`}/>
        </Form>
      </Container>
    )
  }
}

export default MagicLinkAdderModal;