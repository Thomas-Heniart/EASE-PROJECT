import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showExtensionDownloadModal} from "../../actions/modalActions";
import {Message,Form,Button} from 'semantic-ui-react';
import {connect} from "react-redux";

class MainView extends Component {
  constructor(props){
    super(props);
    this.state = {
      step: 0
    }
  }
  onClick = () => {
    if (this.state.step === 0){
      this.setState({step: 1});
      window.open('https://chrome.google.com/webstore/detail/ease/hnacegpfmpknpdjmhdmpkmedplfcmdmp', '_blank');
    } else {
      window.location.reload();
    }
  };
  render() {
    return (
        <Form as="div" class="container" style={{paddingTop: '20px'}}>
          <Form.Field style={{color: "#45C997"}}>
            It seems you don’t have our extension yet! Or it has been disabled lately. Access the Chrome Store to solve this.
          </Form.Field>
          <Form.Field>
            <img class="full_width" src="/resources/images/image_popup_extension.jpg"/>
          </Form.Field>
          <Button
              positive
              className="modal-button"
              onClick={this.onClick}
              content={!this.state.step ? 'ACCESS CHROME STORE' : 'DONE'}/>
        </Form>
    )
  }
};

const InformationView = (props) => {
  return (
      <Form as="div" class="container">
        <Form.Field>
          An extension is a small piece of software that you add to your browser in order to customize its capabilities.
        </Form.Field>
        <Form.Field>
          Our extension helps us log you in and out of the websites you need.
        </Form.Field>
        <Form.Field>
          It also keeps your personal information secured. Downloading it takes about 5 seconds!
        </Form.Field>
        <Button
            positive
            onClick={props.goBack}
            className="modal-button"
            content="OK I GOT IT"/>
      </Form>
  )
};

const BrowserDownloadView = (props) => {
  return (
      <Form as="div" class="container">
        <Form.Field>
          Ease.space works on Chrome!
        </Form.Field>
        <Form.Field>
          We are working on bringing it to more browsers.
        </Form.Field>
        <Form.Field>
          To continue using Ease.space, you can download Chrome.
        </Form.Field>
        <Button
            positive
            onClick={e => {window.open('https://www.google.fr/chrome/browser/desktop', '_blank')}}
            className="modal-button"
            content="DOWNLOAD CHROME"/>
      </Form>
  )
};

@connect()
class ExtensionDownloadModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      title: 'Make Ease.space work! ⚡',
      view: 'Download',
      isChrome: !!window.chrome && !!window.chrome.webstore
    }
  }
  close = () => {
    this.props.dispatch(showExtensionDownloadModal({active: false}));
  };
  changeView = (title, view) => {
    this.setState({title: title, view: view});
  };
  render(){
    const {view, isChrome, title} = this.state;
    return (
        <SimpleModalTemplate
            onClose={this.close}
            id={isChrome ? 'download_extension' : null}
            headerContent={title}>
          {view === 'Download' && isChrome &&
          <MainView showInfo={this.changeView.bind(null, 'Our browser extension', 'Information')}/>}
          {view === 'Download' && !isChrome &&
          <BrowserDownloadView/>}
          {view === 'Information' &&
          <InformationView goBack={this.changeView.bind(null,'Make Ease.space work! ⚡', 'Download')}/>}
        </SimpleModalTemplate>
    )
  }
}

export default ExtensionDownloadModal;