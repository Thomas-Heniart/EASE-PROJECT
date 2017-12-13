import React from 'react';
import {connect} from "react-redux";
import { getClearbitLogo } from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import { Input, Button, Icon, Segment, Container } from 'semantic-ui-react';
import { NavLink } from 'react-router-dom';

@connect(store => ({
  catalog: store.catalog
}))
class AddAnyApp extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: '',
      url: '',
      img_url: '/resources/icons/link_app.png'
    };
  }
  handleInput = handleSemanticInput.bind(this);
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    });
  };
  changeUrl = (e, {value}) => {
    this.setState({url: value}, this.getLogo);
  };
  send = () => {

  };
  render() {
    return (
      <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
        <p>Add a website</p>
        <Segment clearing className="addBookmark">
          <NavLink to={`/main/catalog`}>
            <Icon name="close" link class="closeButton"/>
          </NavLink>
          <div className="display_flex">
            <div className="logo">
              <img src={this.state.img_url} alt="website logo"/>
            </div>
            <div className="main_column width100">
              <div className="display-inline-flex width100">
                <Input placeholder="Paste website URL"
                       className="width100"
                       autoComplete="off"
                       type="url"
                       name="url"
                       value={this.state.url}
                       onChange={this.changeUrl}
                       size="mini"
                       fluid
                       required />
              </div>
              <div className="display-inline-flex width100">
                <Input  className="width100"
                        placeholder="Name"
                        name="name"
                        fluid
                        value={this.state.name}
                        autoComplete="off"
                        onChange={this.handleInput}
                        size="mini"
                        required />
              </div>
            </div>
          </div>
          <Button positive
                  size="mini"
                  floated="right">
            <Icon name="arrow right" />
            Next
          </Button>
        </Segment>
      </Container>
    )
  }
}

export default AddAnyApp;