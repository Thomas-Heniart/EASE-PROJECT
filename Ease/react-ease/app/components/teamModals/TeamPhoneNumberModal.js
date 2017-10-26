var React = require('react');
var classnames = require('classnames');
import {Button, Form, Message, Header} from 'semantic-ui-react';
import {showTeamPhoneNumberModal} from "../../actions/teamModalActions";
import {selectUserFromListById} from "../../utils/helperFunctions";
import {editTeamUserPhone} from "../../actions/userActions";
import {connect} from "react-redux";

@connect((store) => {
    return {
        team: store.team,
        users: store.users.users
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
        const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
        this.setState({loading: true, errorMessage: ''});
        this.props.dispatch(editTeamUserPhone(me.id, this.state.phone)).then(r => {
            this.props.dispatch(showTeamPhoneNumberModal(false));
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