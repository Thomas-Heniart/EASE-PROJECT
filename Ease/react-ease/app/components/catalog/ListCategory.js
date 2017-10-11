import React from 'react';
import { List } from 'semantic-ui-react';
import { render } from 'react-router-dom';

class ListCategory extends React.Component {
    constructor(props){
        super(props);
    }

    render() {
        return (
            <List link className="listCategory">
                <List.Item onClick={e => this.props.showAllApps(e)}>All</List.Item>
                {this.props.categories.map((item, key) =>
                    <List.Item key={key} onClick={e => this.props.sortList(e, item.name)}>{item.name}</List.Item>
                )}
            </List>
        )
    };
}

export default ListCategory;