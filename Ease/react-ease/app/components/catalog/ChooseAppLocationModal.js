import React from 'react';
import {connect} from "react-redux";
import {dashboard} from "../../utils/post_api";
import {reduxActionBinder} from "../../actions/index";
import {handleSemanticInput} from "../../utils/utils";
import ChooseAppRoomLocationModal from "./ChooseAppRoomLocationModal";
import { Button, Form, Checkbox, Label, Icon } from 'semantic-ui-react';

@connect(store => ({
    teams: store.teams,
    dashboard: store.dashboard
}), reduxActionBinder)
class ChooseAppLocationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
    }
  }
  changeView = () => {
    if (this.state.view === 1 && this.props.check !== 'newApp')
      this.setState({view: 2});
    else
      this.props.confirm();
  };
  handleInput = handleSemanticInput.bind(this);
  render() {
    const {
      check,
      website,
      appName,
      checkRoom,
      selectTeam,
      selectRoom,
      selectProfile,
    } = this.props;
    const teams = Object.keys(this.props.teams).filter(team_id => {
      const team = this.props.teams[team_id];
      return team.onboarding_step === 5 && !team.team_users[team.my_team_user_id].disabled;
    }).map(team_id => (
      <Checkbox radio
                name='check'
                key={team_id}
                value={team_id}
                checked={check === team_id}
                style={{margin: "0 0 10px 0"}}
                onChange={e => selectTeam(team_id)}
                label={this.props.teams[team_id].name}/>));
    return (
      <Form class="container" id="add_bookmark_form" onSubmit={this.changeView}>
        <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
          {website.logo ?
            <div className="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            :
            <div className="squared_image_handler" style={{
              backgroundColor: '#373b60', color: 'white', fontSize: '24px', backgroundSize: 'cover', display: 'flex'
            }}>
              <div style={{margin: 'auto'}}>
                <p style={{margin: 'auto'}}>{this.props.logoLetter}</p>
              </div>
            </div>}
          {this.state.view === 1 &&
          <span className="app_name">{appName}</span>}
          {this.state.view === 2 &&
          <div className="team_app_settings_name">
            <div>
              <span className="app_name">{appName}</span>
            </div>
            <div>
              <Label className="team_name" icon={<Icon name="users" class="mrgnRight5"/>} size="tiny"
                     content={this.props.teams[Number(check)].name}/>
            </div>
          </div>}
        </Form.Field>
        {this.state.view === 1 &&
        <React.Fragment>
          <div style={{marginBottom: '10px', fontWeight: 'bold'}}>Where would you like to send this app?</div>
          <Form.Field className='choose_type_app'>
            {(website.information && Object.keys(website.information).length > 0) && teams}
            {this.props.bookmark && teams}
            <Checkbox radio
                      name='check'
                      value='newApp'
                      label='Personal Account'
                      onChange={selectProfile}
                      checked={check === 'newApp'}/>
          </Form.Field>
        </React.Fragment>}
        {this.state.view === 2 &&
        <ChooseAppRoomLocationModal team_id={Number(check)} checkRoom={checkRoom} handleChange={selectRoom} back={check}/>}
        <Button
          attached='bottom'
          type="submit"
          positive
          loading={this.props.loading}
          disabled={check === null || appName.length === 0 || this.props.loading}
          onClick={this.changeView}
          className="modal-button"
          content="NEXT"/>
      </Form>
    )
  }
}

export default ChooseAppLocationModal;