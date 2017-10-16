import React from 'react';
import { Container, Segment, Button, Input, Image, Icon, Label } from 'semantic-ui-react';
import { render } from 'react-router-dom';
import { getClearbitLogo } from "../../utils/api";
import {selectUserFromListById} from "../../utils/helperFunctions";
import * as appActions from "../../actions/appsActions";
import {teamCreateLinkAppNew} from "../../actions/appsActions";
import {requestWebsite, showPinTeamAppToDashboardModal} from "../../actions/teamModalActions";
import {connect} from "react-redux";

class AddBookmark extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            appName: '',
            url: '',
            logoSrc: '/resources/icons/link_app.png',
            img_url: '',
            comment: '',
            selectedUsers: [],
            loading: false,
            users: [],
        };
        this.handleAppNameChange = this.handleAppNameChange.bind(this);
        this.handleUrlInput= this.handleUrlInput.bind(this);
        this.handleComment = this.handleComment.bind(this);
    }

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
        this.setState({loading: true});
    };
    handleComment(event){
        this.setState({comment: event.target.value});
    }
    handleAppNameChange(event){
        this.setState({appName: event.target.value});
    }
    handleUrlInput(event){
        this.setState({url: event.target.value});
    }
    render(){
        return (
            <Container fluid class="mrgn0" as="form" onSubmit={this.send}>
                <h3>Add a Bookmark</h3>
                <Segment clearing className="addBookmark">
                    <div className="display_flex">
                        <div className="logo">
                            <img src={this.state.img_url ? this.state.img_url : this.state.logoSrc} alt="website logo"/>
                        </div>
                        <div className="main_column width100">
                            <div className="display-inline-flex width100">
                                <Input placeholder="Paste website URL"
                                       className="width100"
                                       autoComplete="off"
                                       type="text"
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
                                        name="bookmark_name"
                                        value={this.state.appName}
                                        autoComplete="off"
                                        onChange={this.handleAppNameChange}
                                        size="mini"
                                        required />
                            </div>
                        </div>
                    </div>
                    <Button positive
                            size="mini"
                            floated="right"
                            loading={this.state.loading}
                            disabled={this.state.loading}>
                        Pin
                        <Icon name="pin" />
                    </Button>
                </Segment>
            </Container>
        )
    }
}

export default AddBookmark;