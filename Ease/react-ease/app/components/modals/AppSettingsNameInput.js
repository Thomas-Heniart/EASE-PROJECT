import React, {Component} from "react";
import {Input,Icon} from 'semantic-ui-react';

class AppSettingsNameInput extends Component {
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
    const {value, onChange} = this.props;

    if (!this.state.modifying)
      return (
          <span class="app_name">{value}<Icon link style={{marginLeft:'5px'}} name="pencil" onClick={this.setModifying}/></span>
      );
    return (
        <Input
            type="text"
            placeholder="App name..."
            name="appName"
            class="input_unstyle modal_input name_input"
            autoFocus={true}
            value={value}
            onChange={onChange}/>
    )
  }
}

export default AppSettingsNameInput;
