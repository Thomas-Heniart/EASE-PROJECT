import React, {Component} from "react";
import {Loader, Input, Label, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showSimpleAppSettingsModal} from "../../actions/modalActions";
import {connect} from "react-redux";

@connect()
class SimpleAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {

    }
  }
  close = () => {
    this.props.dispatch(showSimpleAppSettingsModal({active: false}));
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={"App settings"}>
          <Container>
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={'/resources/websites/Facebook/logo.png'} alt="Website logo"/>
              </div>
              <span style={{fontSize: "1.3rem"}}>Facebook</span>
            </div>
            <Button
                type="submit"
                positive
                className="modal-button"
                content="CONFIRM"/>
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default SimpleAppSettingsModal;