import React from 'react';
import {connect} from "react-redux";
import {logoLetter} from "../../utils/utils";
import { getClearbitLogoAutoComplete } from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import { Input, Container, Button, Icon, Segment } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class AddSoftwareCredentials extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: '',
      img_url: ''
    };
  }
  handleInput = handleSemanticInput.bind(this);
  getLogo = () => {
    const name = this.state.name.replace(/\s+/g, '').toLowerCase();
    if (name === '')
      this.setState({img_url: ''});
    else
      getClearbitLogoAutoComplete(name).then(response => {
        this.setState({img_url: response});
      }).catch(err => {
          this.setState({img_url: ''});
      });
  };
  changeUrl = (e, {name, value}) => {
    this.setState({[name]: value}, this.getLogo);
  };
  imgNone = (e) => {
    e.preventDefault();
    this.setState({img_url:''});
  };
  send = (e) => {
    e.preventDefault();
    this.props.catalogAddSoftwareAppModal({
      active: true,
      name: this.state.name,
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
        <p style={{fontSize:'18px',fontWeight:'bold',color:'#949eb7',marginTop:'20px'}}>Add Software Credentials</p>
        <Segment clearing className="addBookmark">
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
                  <p style={{margin:'auto'}}>{logoLetter(this.state.name)}</p>
                </div>
                :
                  <div style={{backgroundColor:'white',color: '#dededf'}}>
                    <Icon name='wait' style={{margin:'auto'}}/>
                  </div>}
            </div>
            <div className="main_column width100">
              <div className="display-inline-flex width100" style={{marginTop:'4%'}}>
                <Input className="width100"
                       autoFocus
                       placeholder="Name your Software"
                       name="name"
                       fluid
                       value={this.state.name}
                       autoComplete="off"
                       onChange={this.changeUrl}
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