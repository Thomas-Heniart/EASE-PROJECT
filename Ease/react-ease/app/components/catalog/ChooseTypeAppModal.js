import React from 'react';
import { Form, Checkbox, Button, Icon } from 'semantic-ui-react';
import {logoLetter} from "../../utils/utils";
import {withRouter} from 'react-router-dom';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
    card: store.teamCard,
    modal: store.teamModals.catalogAddAppModal,
    teams: store.teams
}), reduxActionBinder)
class ChooseTypeAppModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      website: this.props.website,
      value: 'Simple',
      subtype: this.props.subtype
    }
  }
  handleChange = (e, {value}) => this.setState({value});
  catalogToTeamSpace = (e, team_id, room_id) => {
    e.preventDefault();
    this.props.close();
    this.props.createTeamCard({
      team_id,
      channel_id: room_id,
      website: this.state.website,
      type: this.state.value,
      name: this.state.website.name && !this.props.account_information ? this.state.website.name : this.props.appName,
      url: this.props.url,
      subtype: this.state.subtype,
      account_information: this.props.account_information ? this.props.account_information : -1
    });
    this.props.history.push(`/teams/${team_id}/${room_id}`);
  };
  render() {
    const {
      website,
      appName,
      team_id,
      room_id} = this.props;
    return (
      <Form class="container" id="add_bookmark_form" onSubmit={e => this.catalogToTeamSpace(e, team_id, room_id)}>
          <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
            {website.logo && website.logo !== '' ?
              <div className="squared_image_handler">
                <img src={website.logo} alt="Website logo"/>
              </div>
              :
              <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
                <div style={{margin:'auto'}}>
                  <p style={{margin:'auto'}}>{logoLetter(appName)}</p>
                </div>
              </div>}
              <div className='show_team'>
                  <p className='app'><span className="app_name">{appName}</span></p>
                  <p className='team'><span><Icon name='users'/>{this.props.teams[team_id].name}</span></p>
                  <p className='room'><span># {this.props.teams[team_id].rooms[room_id].name}</span></p>
              </div>
          </Form.Field>
          <Form.Field>
              <div style={{fontWeight:'bold'}}>People who will access this app:</div>
          </Form.Field>
          <Form.Field className='choose_type_app'>
              <Checkbox radio
                        label='Share a common account together'
                        name='checkboxRadioGroup'
                        value='Simple'
                        checked={this.state.value === 'Simple'}
                        onChange={this.handleChange}/>
              <Checkbox radio
                        label='Each use their own nominative account'
                        name='checkboxRadioGroup'
                        value='Multi'
                        checked={this.state.value === 'Multi'}
                        onChange={this.handleChange}/>
          </Form.Field>
          <Button
            attached='bottom'
            type="submit"
            positive
            onClick={e => this.catalogToTeamSpace(e, team_id, room_id)}
            className="modal-button"
            content="NEXT"/>
      </Form>
    )
  }
}

export default withRouter(ChooseTypeAppModal);