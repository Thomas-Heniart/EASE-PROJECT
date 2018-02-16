import React from 'react';
import {connect} from "react-redux";
import {logoLetter} from "../../../utils/utils";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../../utils/utils";
import {Container, Form, Message, Button, Checkbox, Label, Icon} from 'semantic-ui-react';
import ChooseTypeAppModal from '../ChooseTypeAppModal';

@connect(store => ({
    teams: store.teams,
    modal: store.modals.newAccountUpdateLocation
}))
class NewAccountUpdateLocationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: '',
      check: '',
      roomName: [],
      view: 1,
      loading: false,
      seePassword: false,
      website: this.props.modal.website,
      account_information: this.props.modal.account_information
    }
  }
  handleInput = handleSemanticInput.bind(this);
  handleChange = (e, {value}) => this.setState({check: value});
  close = () => {
    this.props.modal.reject();
  };
  edit = () => {
    this.props.modal.resolve();
  };
  changView = () => {
    this.setState({view: 2});
  };
  componentWillMount() {
    let roomName = [];
    Object.keys(this.props.teams[this.props.modal.team].rooms).map(room => {
      roomName.push(this.props.teams[this.props.modal.team].rooms[room]);
    });
    this.setState({roomName: roomName});
  }
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"Choose App location"}>
        <Container className="app_settings_modal">
          {this.state.view === 1 &&
          <Form onSubmit={this.changView} error={this.state.error.length > 0} id='add_bookmark_form'>
            <div className="app_name_container display-flex align_items_center">
              {this.state.website.logo && this.state.website.logo !== '' ?
                <div className="squared_image_handler">
                  <img src={this.state.website.logo} alt="Website logo"/>
                </div>
                :
                <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
                  <div style={{margin:'auto'}}>
                    <p style={{margin:'auto'}}>{logoLetter(this.props.modal.appName)}</p>
                  </div>
                </div>}
              <div className="team_app_settings_name">
                <div>
                  <span className="app_name">{this.props.modal.appName}</span>
                </div>
                <div>
                  <Label className="team_name" icon={<Icon name="users" class="mrgnRight5"/>} size="tiny"
                         content={this.props.teams[this.props.modal.team].name}/>
                </div>
              </div>
            </div>
            <Form.Field>
              <div style={{fontWeight: 'bold'}}>Where would you like to send this app?</div>
            </Form.Field>
            <Form.Field className='choose_type_app'>
              {this.state.roomName.map(room => {
                if (room.id && room.team_user_ids.filter(id => (id === this.props.teams[this.props.modal.team].my_team_user_id)).length > 0)
                  return <Checkbox radio
                                   style={{margin: "0 0 10px 0"}}
                                   name={room.id}
                                   key={room.id}
                                   value={room.id}
                                   onChange={this.handleChange}
                                   label={"#" + room.name}
                                   checked={this.state.check === room.id}/>
              })}
            </Form.Field>
            <Message error content={this.state.error}/>
            <Button
              positive
              type="submit"
              className="modal-button"
              content="NEXT"
              loading={this.state.loading}
              disabled={this.state.loading || this.state.check === ''}/>
          </Form>
          }
        </Container>
        {this.state.view === 2 &&
        <ChooseTypeAppModal
          {...this.props}
          account_information={this.props.modal.account_information}
          subtype={this.props.modal.website.url ? 'AnyApp' : 'classic'}
          website={this.props.modal.website}
          appName={this.props.modal.appName}
          team_id={this.props.modal.team}
          room_id={this.state.check}
          close={this.close}/>
        }
      </SimpleModalTemplate>
    )
  }

}

export default NewAccountUpdateLocationModal;