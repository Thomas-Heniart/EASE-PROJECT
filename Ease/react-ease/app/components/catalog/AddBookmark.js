import React from 'react';
import { Container, Segment, Button, Input, Icon } from 'semantic-ui-react';
import { getClearbitLogo } from "../../utils/api";
import {handleSemanticInput} from "../../utils/utils";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

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
      if (response !== "")
        this.setState({img_url: response});
      else
        this.setState({img_url: '/resources/icons/link_app.png'});
    }).catch(err => {
      this.setState({img_url: '/resources/icons/link_app.png'});
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
      img_url: this.state.img_url,
    }).then(app => {
      this.setState({name: '', url: '', img_url:'/resources/icons/link_app.png'});
    }).catch(() => {

    });
  };
  render(){
    return (
      <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
        <p style={{fontSize:'18px',fontWeight:'bold',color:'#949eb7',marginTop:'20px'}}>Add a Shortcut link</p>
        <Segment clearing className="addBookmark">
          <div className="display_flex">
            <div className="logo">
              <img src={this.state.img_url} alt='bookmark_logo'/>
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
                       autoFocus
                       required />
              </div>
              <div className="display-inline-flex width100">
                <Input  className="width100"
                        placeholder="Name your Bookmark"
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

export default AddBookmark;