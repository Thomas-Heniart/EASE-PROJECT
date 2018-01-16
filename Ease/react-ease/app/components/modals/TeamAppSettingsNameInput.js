import React, {Component} from "react";
import {Input,Icon, Label} from 'semantic-ui-react';

class TeamAppSettingsNameInput extends Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false
    }
  }
  setModifying = () => {
    this.setState({modifying: true});
  };
  render(){
    const {value, onChange, team_name, room_name} = this.props;
    return (
        <div class="display_flex flex_direction_column team_app_settings_name">
          {!this.state.modifying ?
              <span class="app_name">{value}<Icon link style={{marginLeft: '5px'}} size="small" name="pencil"
                                                  onClick={this.setModifying}/></span>
              :
              <Input
                  type="text"
                  placeholder="App name..."
                  name="appName"
                  class="input_unstyle modal_input name_input"
                  autoFocus={true}
                  value={value}
                  onChange={onChange}/>}
          <div>
            <Label class="team_name" icon={<Icon name="users" class="mrgnRight5"/>} size="tiny" content={team_name}/>
          </div>
          <span class="room_name"># {room_name}</span>
        </div>
    )
  }
}

export default TeamAppSettingsNameInput;