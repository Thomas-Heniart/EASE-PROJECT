import React from 'react';
import { List } from 'semantic-ui-react';

class ListCategory extends React.Component {
    constructor(props){
        super(props);
    }

    render() {
        return (
            <List link className="listCategory">
                {this.props.categorySelected.id ?
                    <List.Item as='a' onClick={e => this.props.showAllApps(e)}>All</List.Item>
                    :
                    <List.Item as='a' active onClick={e => this.props.showAllApps(e)}>All</List.Item>
                }
                {this.props.categories.map((item, key) =>
                    this.props.categorySelected.id === item.id
                            ?
                            <List.Item as='a' active key={key}
                                       onClick={e => this.props.sortList(e, item)}>{item.name}</List.Item>
                            :
                            <List.Item as='a' key={key}
                                       onClick={e => this.props.sortList(e, item)}>{item.name}</List.Item>
                )}
            </List>
        )
    };
}

export default ListCategory;