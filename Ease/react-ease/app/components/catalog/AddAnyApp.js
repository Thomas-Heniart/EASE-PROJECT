import React, {Component} from 'react';
import {connect} from "react-redux";
import { getClearbitLogo } from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import { Input, Button, Icon, Segment, Container } from 'semantic-ui-react';
import { NavLink } from 'react-router-dom';
import {reduxActionBinder} from "../../actions/index";

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class AddAnyApp extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: this.props.query,
      url: '',
      img_url: ''
    };
  }
  handleInput = handleSemanticInput.bind(this);
  moveCaretAtEnd = (e) => {
    let temp_value = e.target.value;
    e.target.value = '';
    e.target.value = temp_value;
  };
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    }).catch(err => {
      this.setState({img_url: ''});
    });
  };
  changeUrl = (e, {value}) => {
    this.setState({url: value}, this.getLogo);
  };
  imgNone = (e) => {
    e.preventDefault();
    this.setState({img_url:''});
  };
  logoLetter = () => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < this.state.name.length; letter++) {
      if (first.length < 1 && this.state.name[letter] !== ' ')
        first = this.state.name[letter];
      else if (first.length > 0 && second.length < 1 && this.state.name[letter] !== ' ' && space === true)
        second = this.state.name[letter];
      else if (this.state.name[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
  };
  send = (e) => {
    e.preventDefault();
    this.props.catalogAddAnyAppModal({
      active: true,
      name: this.state.name,
      url: this.state.url,
      img_url: this.state.img_url,
      logoLetter: this.logoLetter()
    }).then(app => {
      this.setState({name: '', url: '', img_url:''});
    }).catch(() => {
    });
  };
  render() {
    return (
      <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
        <p style={{fontSize:'18px',fontWeight:'bold',color:'#949eb7'}}>Add a website</p>
        <Segment clearing className="addBookmark">
          <NavLink to={`/main/catalog/website`}>
            <Icon name="close" link class="closeButton"/>
          </NavLink>
          <div className="display_flex">
            <div className="logo">
              {this.state.img_url ?
                <div style={{backgroundImage:`url('${this.state.img_url}')`}}>
                  <button className="button-unstyle action_button close_button" onClick={this.imgNone}>
                    <Icon name="close" class="mrgn0" link/>
                  </button>
                </div>
                : this.state.name ?
                  <div style={{backgroundColor:'#373b60',color:'white'}}>
                    <p style={{margin:'auto'}}>{this.logoLetter()}</p>
                  </div>
                  :
                  <div style={{backgroundColor:'white',color: '#dededf'}}>
                    <Icon name='wait' style={{margin:'auto'}}/>
                  </div>}
            </div>
            <div className="main_column width100">
              <div className="display-inline-flex width100">
                <Input className="width100"
                       placeholder="Name"
                       name="name"
                       fluid
                       value={this.state.name}
                       autoComplete="off"
                       onChange={this.handleInput}
                       size="mini"
                       autoFocus
                       onFocus={this.moveCaretAtEnd}
                       required />
              </div>
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