import React from 'react';
import {connect} from "react-redux";
import { NavLink } from 'react-router-dom';
import { getClearbitLogo } from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import { Input, Container, Button, Icon, Segment } from 'semantic-ui-react';

@connect(store => ({
}))
class AddSoftwareCredentials extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: '',
      img_url: '/resources/icons/link_app.png'
    };
  }
  handleInput = handleSemanticInput.bind(this);
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    });
  };
  send = (e) => {
  };
  render() {
    return (
      <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
        <p>Add Software Credentials</p>
        <Segment clearing className="addBookmark">
          <NavLink to={`/main/catalog`}>
            <Icon name="close" link class="closeButton"/>
          </NavLink>
          <div className="display_flex">
            <div className="logo">
              <img src={this.state.img_url} alt="website logo"/>
            </div>
            <div className="main_column width100">
              <div className="display-inline-flex width100" style={{marginTop:'4%'}}>
                <Input className="width100"
                       placeholder="Name your Bookmark"
                       name="name"
                       fluid
                       value={this.state.name}
                       autoComplete="off"
                       onChange={this.handleInput}
                       size="mini"
                       required/>
              </div>
            </div>
          </div>
          <Button positive
                  size="mini"
                  floated="right">
            <Icon name="arrow right"/>
            Next
          </Button>
        </Segment>
      </Container>
    )
  }
}

export default AddSoftwareCredentials;