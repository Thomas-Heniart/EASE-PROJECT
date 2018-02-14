import React from 'react';
import {connect} from "react-redux";
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
    console.log('submit');
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
    console.log('STATE : ', this.state);
    console.log('PROPS :', this.props);
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
              <div className="squared_image_handler">
                <img src="/resources/icons/link_app.png" alt="Website Logo"/>
              </div>
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
                return <Checkbox radio
                                 style={{margin: "0 0 10px 0"}}
                                 name={room.id}
                                 key={room.id}
                                 value={room.name}
                                 onChange={this.handleChange}
                                 label={"#" + room.name}
                                 checked={this.state.check === room.name}/>
              })}
            </Form.Field>
            <Message error content={this.state.error}/>
            <Button
              positive
              type="submit"
              className="modal-button"
              content="NEXT"
              loading={this.state.loading}
              disabled={this.state.loading}/>
          </Form>
          }{this.state.view === 2 &&
            <ChooseTypeAppModal
              {...this.props}
              // website={this.state.}
              appName={this.props.modal.appName}
              team_id={this.props.modal.team}
              room_id={this.state.check}
              close={this.close}/>
          }



        </Container>
      </SimpleModalTemplate>
    )
  }

}

export default NewAccountUpdateLocationModal;