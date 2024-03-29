var React = require('react');
var classnames = require('classnames');
import {Button, Form, Message, Header} from 'semantic-ui-react';
import {showTeamPhoneNumberModal} from "../../actions/teamModalActions";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {editTeamUserPhone} from "../../actions/userActions";
import {connect} from "react-redux";

@connect((store) => {
  return {
    team_id: store.teamModals.teamPhoneNumberModal.team_id,
    team_user_id: store.teamModals.teamPhoneNumberModal.team_user_id
  };
})
class TeamPhoneNumberModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      phone: '+33',
      errorMessage: '',
      loading: false
    };
    this.confirm = this.confirm.bind(this);
  }

  confirm() {
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editTeamUserPhone({
      team_id: this.props.team_id,
      team_user_id: this.props.team_user_id,
      phone_number: this.state.phone
    })).then(r => {
      this.props.dispatch(showTeamPhoneNumberModal({active: false}));
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  }

  handleInput = (e, {value}) => {
    this.setState({phone: value});
  };

  render() {
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask"/>
          <div class="ease_popup ease_team_popup">
            <Header as="h3" attached="top">
              You are the team owner
            </Header>
            <Form class="container" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
              <Form.Field>
                <span>As owner, if you lose your password, we will contact you to make sure your password renewal is legitimate.</span>
                <br/>
                <span>Later, you will be able to transfer the ownership to someone else in your team.</span>
              </Form.Field>
              <Form.Field>
                <Form.Input
                    defaultValue={this.state.phone}
                    onChange={this.handleInput}
                    autoFocus={true}
                    type="tel"
                    pattern="^((\+\d{1,3}(-| )?\(?\d\)?(-| )?\d{1,5})|(\(?\d{2,6}\)?))(-| )?(\d{3,4})(-| )?(\d{4})(( x| ext)\d{1,5}){0,1}$"
                    placeholder={"+33 6 26 97 88 97"}
                    onFocus={(e) => {
                      let val = e.target.value;
                      e.target.value = '';
                      e.target.value = val;
                    }}/>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Button
                  type="submit"
                  loading={this.state.loading}
                  disabled={this.state.phone.length < 7 || this.state.loading}
                  positive
                  className="modal-button"
                  content="SAVE"/>
            </Form>
          </div>
        </div>
    )
  }
}

module.exports = TeamPhoneNumberModal;