import React, {Component, Fragment} from "react";
import {Message, Form, Button} from 'semantic-ui-react';
import {connect} from "react-redux";
import post_api from "../../utils/post_api";
import SimpleModalTemplate from "../common/SimpleModalTemplate";

class InitializePasswordScoreFeatureModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
    const {team_id} = this.props;

    this.setState({loading: true, errorMessage: ''});
    post_api.teams.initializeTeamPasswordScoreFeature({
      team_id: team_id
    }).then(response => {
      document.location.reload();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    return (
        <SimpleModalTemplate
            headerContent={
              <Fragment>
                Do you know if your passwords are strong enough?<i className="em-svg em-closed_lock_with_key"/><i className="em-svg em-muscle"/>
              </Fragment>
            }>
          <Form class="container" error={!!this.state.errorMessage.length} onSubmit={this.confirm}>
            <Form.Field>
              Now you’ll be able to know how many passwords of your team are strong enough. This is called the Security Score.
            </Form.Field>
            <Form.Field>
              This feature needs to be initialized and will take about 1mn. Thank you for being patient<i className="em-svg em-raised_hands"/>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                className="modal-button"
                onClick={this.close}
                content="INITIALIZE SECURITY SCORE"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default InitializePasswordScoreFeatureModal;