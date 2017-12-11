import React from 'react';
import { Container, Segment, Button, Input, Image, Icon, Label } from 'semantic-ui-react';
import { getClearbitLogo } from "../../utils/api";
import {handleSemanticInput, isUrl} from "../../utils/utils";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { NavLink } from 'react-router-dom';

@connect(store => ({
}), reduxActionBinder)
class AddBookmark extends React.Component {
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
  send = (e) => {
    e.preventDefault();
    this.props.catalogAddBookmarkModal({
      name: this.state.name,
      url: this.state.url,
      img_url: this.state.img_url
    }).then(app => {
      this.setState({name: '', url: '', img_url:'/resources/icons/link_app.png'});
      this.props.history.push('/main/catalog');
    }).catch(() => {

    });
  };
  render(){
    return (
        <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
            <h3>Add a Bookmark</h3>
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
                        <div className="width100">
                            <Input  className="width50"
                                    placeholder="Name your Bookmark"
                                    name="name"
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
                    Pin
                    <Icon name="pin" />
                </Button>
            </Segment>
        </Container>
    )
  }
}

export default AddBookmark;